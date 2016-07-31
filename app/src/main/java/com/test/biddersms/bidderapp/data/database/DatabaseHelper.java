package com.test.biddersms.bidderapp.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leonardoilagan on 30/07/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    Context mContext;
    // ALL STATIC VARIABLE
    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "bidder.db";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BIDDER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_BIDDER);
        onCreate(db);
    }

    private static final String CREATE_TABLE_BIDDER = "CREATE TABLE " + DBConstants.TABLE_BIDDER + "("
            + DBConstants.BIDDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBConstants.BIDDER_NAME + " TEXT , "
            + DBConstants.BIDDER_MESSAGE + " TEXT "
            +")";

}
