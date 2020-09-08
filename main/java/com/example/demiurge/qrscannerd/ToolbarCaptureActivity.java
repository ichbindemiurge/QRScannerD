package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.journeyapps.barcodescanner.camera.CameraInstance;
import com.journeyapps.barcodescanner.camera.CameraManager;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ToolbarCaptureActivity extends AppCompatActivity {

    private CaptureManager captureManager;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton flashButton;
    private BarcodeView barcodeView;
    private ViewfinderView viewFinder;
    private DecoratedBarcodeView.TorchListener torchListener;
    private Boolean torchOn = true;
    private BeepManager beepManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TOOLBAR_CAPT_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_capture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        //toolbar.setTitle("Scan Barcode");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeView = (BarcodeView) findViewById(R.id.zxing_barcode_surface);
        viewFinder = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);
        flashButton = (ImageButton) findViewById(R.id.flashlight_btn);


        captureManager = new CaptureManager(this, barcodeScannerView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

        beepManager = new BeepManager(this);



        addButtonListener();

        if(savedInstanceState != null)
        {
            ToolbarCaptureActivity.this.onRestoreInstanceState(savedInstanceState);

        }else{
            Log.d("SSSSSSSSSSSSSSSSSS", "TOOLBAR");

        }
    }



    public void isTorchOn()
    {
        barcodeView.setTorch(true);
        if(torchListener != null)
        {
            torchListener.onTorchOn();
        }
    }

    public void isTorchOff()
    {
        barcodeView.setTorch(false);
        if(torchListener !=null)
        {
            torchListener.onTorchOff();
        }
    }
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void addButtonListener()
    {
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(torchOn)
                {
                    isTorchOn();
                    torchOn = false;
                }
                else
                {
                    isTorchOff();
                    torchOn = true;
                }
            }
        });
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



    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
       // cameraManager = new CameraManager(getApplication());
        Log.d("TOOLBAR", "RESUME");

        //cameraManager = new CameraManager(getApplication());
        //viewFinder.setCameraPreview(barcodeView);

    }



    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
        Log.d("TOOLBAR", "PAUSE");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.d("TOOLBAR", "POST_RESUME");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
        Log.d("TOOLBAR", "Destroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        captureManager.onSaveInstanceState(savedInstanceState);
        Log.d("TOOLBAR", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("!!!TOOLBAR RESTORE!!!!", "onRestoreInstanceState");
    }





}
