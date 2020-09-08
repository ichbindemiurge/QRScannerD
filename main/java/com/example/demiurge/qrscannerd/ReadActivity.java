package com.example.demiurge.qrscannerd;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.result.AddressBookDoCoMoResultParser;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.TelParsedResult;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.VCardResultParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.journeyapps.barcodescanner.camera.CameraManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import static com.example.demiurge.qrscannerd.HistoryDBHelper.TABLE_NAME;


public class ReadActivity extends AppCompatActivity {

    private String ResultTAG = "ACTIVITY_RESULT";
    private final String BUNDLE_KEY = "SCANNER_STATE";

    private static int cameraPermissionReqCode = 250;

    private boolean flag = false;
    private boolean scanFlag = true;

    private Activity activity;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    public static final int REQUEST_CODE = 1;

    HistoryDBHelper historyDBHelper;
    HistoryManager historyManager;
    PhoneResultHandlerActivity phoneResultHandlerActivity;
    ResultHandlerClass resultHandlerClass;





    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == cameraPermissionReqCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                barcodeView.resume();
            } else {
                // TODO: display better error message.
            }
        }
    }

    IntentIntegrator scanningIntentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("READ_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);


        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.read_app_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        beepManager = new BeepManager(this);


        historyDBHelper = new HistoryDBHelper(this);
        historyManager = new HistoryManager(this);
        resultHandlerClass = new ResultHandlerClass(ReadActivity.this);


         scanning();
         scanFlag = false;


        if(savedInstanceState != null)
        {
            ReadActivity.this.onRestoreInstanceState(savedInstanceState);

        }else{


        }

    }

    public void scanning()
    {
        Intent startSc = new Intent(ReadActivity.this, ScannerActivity.class);
        startActivityForResult(startSc, 1);
    }


    private void addToHistory(Result result)
    {
       historyManager.addHistoryItem(result);
    }




    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        flag = true;
        scanFlag = false;

        if (requestCode != 0) {
            Log.d("INTENT", "IS NOT NULL");
            if (resultCode == RESULT_OK)
            {

                Log.d("INTENT", "IS OKAY" + intent.getStringExtra("MSG"));


                Result duble = new Result(intent.getStringExtra("MSG"), null, null, BarcodeFormat.QR_CODE);

                addToHistory(duble);


            ParsedResult parserdResult = ResultParser.parseResult(duble);

            Log.d(ResultTAG, "TYPE: " + parserdResult.getType().toString());

            switch (parserdResult.getType()) {
                case ADDRESSBOOK:
                    Log.d(ResultTAG, "ADDRESSBOOK: " + parserdResult.getDisplayResult());

                    resultHandlerClass.addMeCard(parserdResult);


                    break;


                case EMAIL_ADDRESS:

                    resultHandlerClass.sendEmail(parserdResult);


                    break;
                /////////////////////////////////////////////////////////////////////

                case PRODUCT:
                    ProductParsedResult prod = (ProductParsedResult) parserdResult;
                    //Log.d(ResultTAG, "PRODUCT: " + scanResult.getContents());
                    break;

                case URI:
                    URIParsedResult uri = (URIParsedResult) parserdResult;
                   // Log.d(ResultTAG, "URI: " + scanResult.getContents());
                    String uriStr = uri.getURI();

                    if (uri.getURI().length() > 0 && Patterns.WEB_URL.matcher(uriStr).matches()) {
                        Intent uriOpener = new Intent(ReadActivity.this, WebViewActivity.class);
                        uriOpener.putExtra("URI", uriStr);
                        startActivity(uriOpener);
                    }
                    break;


                case GEO:
                    GeoParsedResult geo = (GeoParsedResult) parserdResult;
                   // Log.d(ResultTAG, "GEO: " + scanResult.getContents());

                    Uri geoStr = Uri.parse("geo:" + geo.getLatitude() + ',' + geo.getLongitude());


                    Intent geoOpener = new Intent(Intent.ACTION_VIEW, geoStr);
                    geoOpener.setPackage("com.google.android.apps.maps");

                    if (geoOpener.resolveActivity(getPackageManager()) != null) {
                        startActivity(geoOpener);
                    }

                    break;

                /////////////////////////////////////////////////////////////////////

                //do i want to start the call immediately or do i want to open phone number in my preffered app
                //and then decide whether or not to make a phone call?


                case TEL:

                    resultHandlerClass.sendPhoneN(parserdResult);




                    break;

                case SMS:
                   // Log.d(ResultTAG, "SMS: " + scanResult.getContents());
                    SMSParsedResult sms = (SMSParsedResult) parserdResult;
                    Log.d(ResultTAG, "SMS: " + sms.getNumbers()[0]);

                    String smsUri = sms.getSMSURI();
                    String smsNum = sms.getNumbers()[0];

                    if (sms.getSMSURI().length() > 0 && Patterns.PHONE.matcher(smsNum).matches()) {
                        Intent smsSender = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + smsNum));
                        smsSender.putExtra(Intent.EXTRA_PHONE_NUMBER, smsNum);
                        smsSender.putExtra(Intent.EXTRA_TEXT, sms.getBody());
                        startActivity(smsSender);
                    }
                    break;


                case CALENDAR:
                   // Log.d(ResultTAG, "CALENDAR: " + scanResult.getContents());
                    CalendarParsedResult calendParRes = (CalendarParsedResult) parserdResult;

                    String calendarEvent = calendParRes.getDisplayResult();
                    //Calendar beginTime = Calendar.getInstance();
                    //beginTime.setTime(calendParRes.getStart());
                    //beginTime.getTimeInMillis();


                    if (calendParRes.getDisplayResult().length() > 0) {
                        Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
                        calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendParRes.getStartTimestamp());
                        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendParRes.getEndTimestamp());
                        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, calendParRes.isStartAllDay()); //isEndAllDay() ???
                        calendarIntent.putExtra(CalendarContract.Events.TITLE, calendParRes.getSummary());
                        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, calendParRes.getDescription());
                        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, calendParRes.getLocation());
                        startActivity(calendarIntent);
                    }


                    break;

                /////////////////////////////////////////////////////////////////////

                case ISBN:
                    ISBNParsedResult isbn = (ISBNParsedResult) parserdResult;
                    //Log.d(ResultTAG, "ISBN: " + scanResult.getContents());
                    break;


                case TEXT:
                        resultHandlerClass.showTextResult(parserdResult);

                    break;

                case WIFI:
                        resultHandlerClass.showWIFIInfo(parserdResult);

                    break;


                /////////////////////////////////////////////////////////////////////
                default:
                    Log.d("TAG", "HOLA");
            }
        }
        }
        else
        {
           Toast.makeText(this, "Not working again", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if(scanFlag)
        {
            scanning();

        }
        Log.d("READ_ACTIVITY", "RESUME");
        scanFlag = true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


        Log.d("READ_ACTIVITY", "POST_RESUME");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "HOLA");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("READ", "DESTROY");
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("!!!!!!!!!!READ!!!!!!", "WORKS");

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("!!!READ RESTORE!!!!!!", "onRestoreInstanceState");
    }



}
