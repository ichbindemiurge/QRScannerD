package com.example.demiurge.qrscannerd;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
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
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoderFactory;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.journeyapps.barcodescanner.CaptureManager.resultIntent;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = ScannerActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Result olo;
    private CaptureManager captureManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        //toolbar.setTitle("Scan Barcode");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        barcodeView.setStatusText("Please place QR code in the viewfinder");
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

    }

    private String ResultTAG = "ACTIVITY_RESULT";


    public void sendResults(String res)
    {
        Intent sendRes = new Intent();
        sendRes.putExtra("Msg", res);
        setResult(RESULT_OK, sendRes);
        Log.d("FUNC", res);
        finish();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterOptions = getMenuInflater();
        inflaterOptions.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.generate_dropdown:
                //generating qr
                Toast.makeText(getApplicationContext(), "Generating", Toast.LENGTH_SHORT).show();
                Intent generateIntent = new Intent(this, GenerateActivity.class);
                startActivity(generateIntent);
                break;

            case R.id.history_dropdown:
                //saved scanned results
                Toast.makeText(getApplicationContext(), "See your scanned results here", Toast.LENGTH_SHORT).show();
                Intent historyIntent = new Intent (this, HistoryActivity.class);
                startActivity(historyIntent);
                break;

            case R.id.about:
                //about app information
                Toast.makeText(getApplicationContext(), "This is QR Scanner App", Toast.LENGTH_SHORT).show();
                break;

            default:
                //unknown error
        }
        return super.onOptionsItemSelected(item);
    }




    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            if(result.getText() == null || result.getText().equals(lastText)) {

                return;
            }

            BarcodeFormat textFormatResult = result.getBarcodeFormat();

            if(textFormatResult != BarcodeFormat.QR_CODE)
            {
                barcodeView.setStatusText("This is not a QR Code");
                return;
            }


            lastText = result.getText();
            barcodeView.setStatusText(result.getText()); //just check for me, to ensure it recognized something
            beepManager.playBeepSoundAndVibrate();
            Log.d(TAG , result.getResult().toString());
            sendResults(lastText);


        }

       @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };




        @Override
    protected void onResume()
    {
        super.onResume();

        barcodeView.resume();

        Log.d(TAG, "ONResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        barcodeView.pause();

        Log.d(TAG, "ONPause");
    }


    public void pause(View view) {
        barcodeView.pause();

    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
