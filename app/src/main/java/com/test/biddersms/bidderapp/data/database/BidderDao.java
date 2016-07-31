package com.test.biddersms.bidderapp.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.biddersms.bidderapp.data.pojo.Bidder;

/**
 * Created by leonardoilagan on 30/07/2016.
 */

public class BidderDao extends ModelDao<Bidder> {
    public BidderDao(Context context) {
        super(context);
    }

    public long insertNumber(Bidder bidder) throws SQLException {
      boolean isExist =   isExistData(DBConstants.BIDDER_NAME,bidder.getMobileNumber());
        if(isExist){
            return -1;
        }

        open();
        long rowInsert = 0;
        ContentValues values = new ContentValues();
        values.put(DBConstants.BIDDER_NAME, bidder.getMobileNumber());
        rowInsert = database.insert(DBConstants.TABLE_BIDDER, null, values);
        close();
        return rowInsert;
    }



    /**
     * Get all bidders
     */
    public List<Bidder> getAll() throws SQLException {
        open();
        List<Bidder> bidderList = new ArrayList<Bidder>();
        String selectQuerry = "SELECT * FROM " + DBConstants.TABLE_BIDDER;
        Cursor cursor = database.rawQuery(selectQuerry,null);
        if (cursor.moveToFirst()) {
            do {
                Bidder bidder = new Bidder(cursor);
                bidderList.add(bidder);
            } while (cursor.moveToNext());
        }
        close();
        return bidderList;
    }
    /**
     * Delete
     */
    public void delete(Bidder bidder) throws SQLException {
        database.delete(DBConstants.TABLE_BIDDER, DBConstants.BIDDER_NAME
                + " = ? ", new String[]{String.valueOf(bidder.getMobileNumber())});
        close();
    }

    /**
     * check existing data from db
     * @param columnname
     * @param param
     * @return
     * @throws SQLException
     */
    public boolean isExistData(String columnname,String param) throws  SQLException{
        open();
        String selectSql = String.format(
                "SELECT "+DBConstants.BIDDER_NAME+" from "+DBConstants.TABLE_BIDDER+" where "+columnname+" = \"%s\" limit 1", param);
        Cursor cursor = database.rawQuery(selectSql, null);
        boolean result = cursor.moveToFirst();
        close();
        return result;
    }
}
