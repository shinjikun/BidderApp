package com.test.biddersms.bidderapp.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.test.biddersms.bidderapp.data.database.BidderDao;
import com.test.biddersms.bidderapp.data.pojo.Bidder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leonardoilagan on 30/07/2016.
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle myBundle = intent.getExtras();
        SmsMessage [] messages = null;
        if (myBundle != null)
        {

            Object [] pdus = (Object[]) myBundle.get("pdus");
            BidderDao bidderDao = new BidderDao(context);
            messages = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < messages.length; i++)
            {

                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append(messages[i].getMessageBody());

            }
            String messageB = sb.toString();
            //checks if the message follows the format
            boolean  isValid =checkContentMessage(messageB);
            //if yes check the sender if it a bidder from db
            if(isValid){


                matchSender(messages[0].getDisplayOriginatingAddress(),messageB,bidderDao);
            }




        }
    }

    /**
     * checks the format of the sms message
     * @param message
     * @return
     */
    private boolean checkContentMessage(String message){
        if(message.startsWith("#")){
            if(message.length()>=4)
                return true;
        }


        return false ;

    }




    private void matchSender(String senderNumber, String message, BidderDao bidderDao){
        try {
            int index =-1;
            List<Bidder> arryList =  bidderDao.getAll();
            for(int i=0; i< arryList.size(); i++){
                Bidder bidder = arryList.get(i);
                String  mobFormantted =  bidder.getMobileNumber();
                if(mobFormantted.startsWith("0")){
                    mobFormantted = mobFormantted.substring(1,bidder.getMobileNumber().length());

                }
                if(senderNumber.contains(mobFormantted)){
                    index = i ;
                    break;
                }

            }

            if(index!=-1){
                sendSMS(index,arryList,message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * send sms message contains bids to all bidders
     * @param index
     * @param bidders
     * @param message
     */
    private void sendSMS(int index, List<Bidder> bidders, String message){
        // fetch the Sms Manager
        SmsManager sms = SmsManager.getDefault();
        index = index+1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bidder ");
        stringBuilder.append(index);
        stringBuilder.append(": ");
        stringBuilder.append(message.substring(1,message.length()));
        for(int i =0;i< bidders.size();i++){
            Bidder bidder = bidders.get(i);
            sms.sendTextMessage(bidder.getMobileNumber(), null, stringBuilder.toString(), null, null);
        }
    }







}
