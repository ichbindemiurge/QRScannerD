package com.example.demiurge.qrscannerd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.zxing.Result;

/**
 * Created by demiurge on 16.02.18.
 */

public class HistoryDBHelper extends SQLiteOpenHelper {

    public String TAG = HistoryManager.class.getSimpleName();


    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "barcode_scanner_history.db";
    static final String TABLE_NAME = "history";

    //columns names
    static final String ID_COL = "id";
    static final String TEXT_COL = "text";
    static final String FORMAT_COL = "format";
    //static final String TYPE_COL = "type";
    //static final String DISPLAY_COL = "display";
    static final String TIMESTAMP_COL = "timestamp";
    static final String DETAILS_COL = "details";

    HistoryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                //" DROP TABLE " + TABLE_NAME);
                "CREATE TABLE " + TABLE_NAME + " (" +
                        ID_COL + " INTEGER PRIMARY KEY, " +
                        TEXT_COL + " TEXT, " +
                        FORMAT_COL + " TEXT, " +
                        //DISPLAY_COL + " TEXT, " +
                        //TYPE_COL + "TEXT, " +
                        TIMESTAMP_COL + " INTEGER, " +
                        DETAILS_COL + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
