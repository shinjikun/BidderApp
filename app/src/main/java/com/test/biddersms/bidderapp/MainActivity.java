package com.test.biddersms.bidderapp;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.test.biddersms.bidderapp.data.database.BidderDao;
import com.test.biddersms.bidderapp.data.pojo.Bidder;
import com.test.biddersms.bidderapp.listener.MessageReceiver;

import io.fabric.sdk.android.Fabric;
import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MessageReceiver otp;
    private boolean isRegister = false;
    private BidderDao bidderDB ;
    private List<Bidder> arryList;
    private TextView txtList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        txtList =  (TextView)findViewById(R.id.txtList);
        otp = new MessageReceiver();
        bidderDB = new BidderDao(this);
       initList();
    }

    private void initList() {
        txtList.setText("");
        try {
            StringBuilder  stringBuilder = new StringBuilder();
            arryList = bidderDB.getAll();
            for(int  i = 0; i <arryList.size();i++){
                Bidder  bidder  =  arryList.get(i);
                stringBuilder.append(bidder.getMobileNumber());
                stringBuilder.append(" , ");
            }
            txtList.setText(stringBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //starts explicitly the broadcast listner of sms
        startBroadCastListener();
    }




    @Override
    protected void onPause() {
        super.onPause();
        if(isRegister)
            //stop listner when not focus
            unregisterReceiver(otp);

    }

    private void messageToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void startBroadCastListener(){
        // register broadcast receiver Now
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otp, filter);
        isRegister = true;
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button:
                EditText editText = (EditText) findViewById(R.id.editText);
                String mobilenumber = editText.getText().toString();
                if(mobilenumber.isEmpty()){

                    messageToast("Please enter a mobile number");
                    return ;
                }

                Bidder bidder = new Bidder();
                bidder.setMobileNumber(mobilenumber);
                try {
                 long bidder_id =   bidderDB.insertNumber(bidder);
                    if(bidder_id ==-1){
                        messageToast("mobile number already existed on the list");
                    }
                    else {
                      bidder.setId((int)bidder_id);

                    }
                    initList();
                    //clear afterwards
                    editText.setText("");
                    //close the on-screen keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }





}
