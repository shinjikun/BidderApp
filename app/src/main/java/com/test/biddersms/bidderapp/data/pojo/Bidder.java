package com.test.biddersms.bidderapp.data.pojo;

import android.database.Cursor;

import com.test.biddersms.bidderapp.data.database.DBConstants;

/**
 * Created by leonardoilagan on 30/07/2016.
 */

public class Bidder {

    String mobileNumber;
    String message;
    int id;


    public Bidder(Cursor cursor) {
        this.id =cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.BIDDER_ID));
        this.mobileNumber =cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.BIDDER_NAME));
        this.message = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.BIDDER_MESSAGE));
    }

    public Bidder() {
        this.message =  "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
