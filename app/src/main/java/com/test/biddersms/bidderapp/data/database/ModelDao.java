package com.test.biddersms.bidderapp.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

/**
 * Created by leonardoilagan on 30/07/2016.
 */

public abstract class ModelDao<T> {
    protected SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ModelDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
