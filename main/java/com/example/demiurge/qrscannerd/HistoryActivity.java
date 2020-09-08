package com.example.demiurge.qrscannerd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.camera.CameraManager;

import java.util.ArrayList;

import static com.example.demiurge.qrscannerd.HistoryDBHelper.TABLE_NAME;

public class HistoryActivity extends AppCompatActivity {

    HistoryManager historyManager;
    public ListView historyListView;
    private ArrayAdapter<HistoryClass> adapter;
    private Result savedResultToShow;
    CharSequence history;
    ListAdapter listAdapter;
    ResultHandlerClass resultHandlerClass;



    public void checkListViewOpenening(Result result)
    {
        Result inHistory = result;
        ParsedResult parsedResult = ResultParser.parseResult(inHistory);

       // if (Patterns.EMAIL_ADDRESS.matcher(HistoryDBHelper.TYPE_COL).matches())
        {
            Log.d("Res", "EMAIL_ADDRESS: " + parsedResult.getDisplayResult());
            EmailAddressParsedResult email = (EmailAddressParsedResult) parsedResult;

            if (email.getTos().length > 0) {
                Log.d("History_Ac", "E-mail: " + email.getTos()[0] + " Subject: " + email.getSubject() + " Body: " + email.getBody());

                String emailToStr = email.getTos()[0]; //only one recipient for the email
                Intent emailOpener = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailToStr));
                emailOpener.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());
                emailOpener.putExtra(Intent.EXTRA_TEXT, email.getBody());
                startActivity(Intent.createChooser(emailOpener, "Choose your email app"));
            }
        }

    }

    public void getResult(Result result)
    {
        Result gotFromManager = result;

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //checkListViewOpenening(gotFromManager);
            }
        });
    }





//https://www.youtube.com/watch?v=SK98ayjhk1E

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("HISTORY_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.history_back_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);

        historyListView = (ListView) findViewById(R.id.history_listview);
        historyManager = new HistoryManager(this);
        resultHandlerClass = new ResultHandlerClass(HistoryActivity.this);





        final ArrayList<String> historyList = new ArrayList<>();
        final Cursor historyData = historyManager.getHistoryContents();

        if(historyData.getCount() == 0)
        {
            Log.d("---------DATABASE_CHECK", "As empty as your heart");
        }
        else
        {
            while(historyData.moveToNext())
            {
                String contentQR = historyData.getString(1);
                String formatQR = historyData.getString(2);
                String dataType = historyData.getString(4);
                String timeQr = historyData.getString(3);


                StringBuilder builderResult = new StringBuilder();
                builderResult.append(contentQR).append('\n').append(formatQR).append('\n').append(dataType)
                        .append('\n').append(timeQr);
                String finalResult = builderResult.toString();

                historyList.add(finalResult);
                //historyList.add(historyData.getString(1) + (historyData.getString(2)) + (historyData.getString(3)) +
                      //  (historyData.getString(4)));
                //historyList.add(historyData.getString(2));
                //historyList.add(historyData.getString(3));

                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
                historyListView.setAdapter(listAdapter);
                //CharSequence history = historyManager.buildHistory();
                //historyList.add(history.toString());
            }
        }



        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //HistoryClass historyClass = historyManager.buildHistoryItem(position);
                String str = historyListView.getItemAtPosition(position).toString();
                String resultToShow = historyList.get(position);
                HistoryClass historyClass = historyManager.getHistoryItem(position);
                Log.d("onItemClick", str);
                showRes(historyClass.getResult());
                //showRes(historyClass.getResult());
                //It opens the next one i click on, though, processes the data the way it intended to
                // need to check the cursor thingy so it will save the order

            }
        });


    }


    private void showRes(Result result)
    {
        Result inHistory = result;
        ParsedResult parsedResult = ResultParser.parseResult(inHistory);
        Log.d("SHOW_RES_HISTORY_AC", "TYPE: " + parsedResult.getType().toString());


        switch (parsedResult.getType())
        {
            case TEL:
                resultHandlerClass.sendPhoneN(parsedResult);
                break;

            case EMAIL_ADDRESS:
                resultHandlerClass.sendEmail(parsedResult);
                break;

            case ADDRESSBOOK:
                resultHandlerClass.addMeCard(parsedResult);
                break;
            case TEXT:
                resultHandlerClass.showTextResult(parsedResult);

        }


    }



}
