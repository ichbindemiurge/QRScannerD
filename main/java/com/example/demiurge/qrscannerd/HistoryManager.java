package com.example.demiurge.qrscannerd;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by demiurge on 10.03.18.
 */

public class HistoryManager {

    public String TAG = HistoryManager.class.getSimpleName();

    private static final int MAX_ITEMS = 2000;

    private static final String[] COLUMNS = {
            HistoryDBHelper.TEXT_COL,
            //HistoryDBHelper.DISPLAY_COL,
            HistoryDBHelper.FORMAT_COL,
            HistoryDBHelper.TIMESTAMP_COL,
            HistoryDBHelper.DETAILS_COL,
    };

    private static final String[] COUNT_COLUMN = { "COUNT(1)" };

    private static final String[] ID_COL_PROJECTION = { HistoryDBHelper.ID_COL };
    private static final String[] ID_DETAIL_COL_PROJECTION = { HistoryDBHelper.ID_COL, HistoryDBHelper.DETAILS_COL };
    private static final Pattern DOUBLE_QUOTE = Pattern.compile("\"", Pattern.LITERAL);


    private final Activity activity;
    HistoryActivity historyActivity;

    public HistoryManager(Activity activity)
    {
        this.activity = activity;
    }


    public HistoryClass buildHistoryItem(int number) {
        SQLiteOpenHelper helper = new HistoryDBHelper(activity);
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor cursor = db.query(HistoryDBHelper.TABLE_NAME,
                     COLUMNS,
                     null, null, null, null,
                     HistoryDBHelper.TIMESTAMP_COL + " DESC")) {

            cursor.move(number + 1); //number + 1
            String text = cursor.getString(0);
            //String display = "null";
            String format = cursor.getString(1);
            long timestamp = cursor.getLong(2);
            String details = cursor.getString(3);
            Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
            return new HistoryClass(result, details);
        }
    }




    CharSequence buildHistory() {
        StringBuilder historyText = new StringBuilder(1000);
        SQLiteOpenHelper helper = new HistoryDBHelper(activity);
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor cursor = db.query(HistoryDBHelper.TABLE_NAME,
                     COLUMNS,
                     null, null, null, null,
                     HistoryDBHelper.TIMESTAMP_COL + " DESC")) {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            while (cursor.moveToNext()) {

                historyText.append('"').append(massageHistoryField(cursor.getString(0))).append("\",");
                historyText.append('"').append(massageHistoryField(cursor.getString(1))).append("\",");
                historyText.append('"').append(massageHistoryField(cursor.getString(2))).append("\",");


                long timestamp = cursor.getLong(3);
                historyText.append('"').append(massageHistoryField(format.format(timestamp))).append("\",");

            }
        } catch (SQLException sqle) {
            Log.w(TAG, sqle);
        }
        return historyText;
    }

    private static String massageHistoryField(String value) {
        return value == null ? "" : DOUBLE_QUOTE.matcher(value).replaceAll("\"\"");
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
          SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public String displayDataType(ParsedResult parsedResult)
    {
        String resultLabel = null;

        switch (parsedResult.getType())
        {
            case ADDRESSBOOK:
            {
                resultLabel = "Contact";
                break;
            }

            case EMAIL_ADDRESS:
            {
                resultLabel = "Email address";
                break;
            }

            case PRODUCT:
            {
                resultLabel = "Product";
                break;
            }

            case URI:
            {
                resultLabel = "Website address";
                break;
            }

            case GEO:
            {
                resultLabel = "Geolocation coordinates";
                break;
            }

            case TEL:
            {
                resultLabel = "Phone number";
                break;
            }

            case SMS:
            {
                resultLabel = "SMS";
                break;
            }

            case CALENDAR:
            {
                resultLabel = "Calendar event";
                break;
            }

            case ISBN:
            {
                resultLabel = "Book information";
                break;
            }

            case WIFI:
            {
                resultLabel = "Wifi information";
                break;
            }

            case TEXT:
            {
                resultLabel = "Text";
                break;
            }

        }

        return resultLabel;
    }



    public void addHistoryItem(Result result)
    {

        Result inHistory = result;
        ParsedResult parsedResult = ResultParser.parseResult(inHistory);

        String dateRecord = getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS");
        String rawDataType = parsedResult.getType().toString();
        Log.d("DATE_TYPE", rawDataType);
        String dataType = displayDataType(parsedResult);

        ContentValues values = new ContentValues();
        values.put(HistoryDBHelper.TEXT_COL, result.getText());
        values.put(HistoryDBHelper.FORMAT_COL, result.getBarcodeFormat().toString());
        //values.put(HistoryDBHelper.TYPE_COL, parsedResult.getType().toString());
        //values.put(HistoryDBHelper.DISPLAY_COL, handler.getDisplayContents().toString());
        values.put(HistoryDBHelper.TIMESTAMP_COL, dateRecord); //System.currentTimeMillis()
        values.put(HistoryDBHelper.DETAILS_COL, dataType);
        //values.put(HistoryDBHelper.DETAILS_COL, rawDataType);


       SQLiteOpenHelper helper = new HistoryDBHelper(activity);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.insert(HistoryDBHelper.TABLE_NAME, HistoryDBHelper.TIMESTAMP_COL, values);
        } catch (SQLException sqle) {
            Log.w(TAG, sqle);
        }

        Log.d(TAG, HistoryDBHelper.TEXT_COL);


        //historyActivity.getResult(inHistory);


    }

    public Cursor getHistoryContents()
    {
        SQLiteOpenHelper helper = new HistoryDBHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor historyData = db.rawQuery("SELECT * FROM " + HistoryDBHelper.TABLE_NAME, null);

        return historyData;
    }


    public HistoryClass getHistoryItem(int number) {
        SQLiteOpenHelper helper = new HistoryDBHelper(activity);
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor cursor = db.query(HistoryDBHelper.TABLE_NAME,
                     COLUMNS,
                     null, null, null, null,
                     null)) {

            cursor.move(number + 1); //number + 1
            String text = cursor.getString(0);
            //String display = "null";
            String format = cursor.getString(1);
            long timestamp = cursor.getLong(2);
            String details = cursor.getString(3);
            Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
            return new HistoryClass(result, details);

        }
    }


}
