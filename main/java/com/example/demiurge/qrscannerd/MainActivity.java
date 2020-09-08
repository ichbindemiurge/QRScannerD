package com.example.demiurge.qrscannerd;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MAIN_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent startScan = new Intent (MainActivity.this, ReadActivity.class);
        startActivity(startScan);

       /* Intent launchReadAc = new Intent(MainActivity.this, ReadActivity.class);
        MainActivity.this.startActivity(launchReadAc); */


        //Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);

        if(savedInstanceState != null)
        {
            MainActivity.this.onRestoreInstanceState(savedInstanceState);
            // startActivity(startScan);

        }else{

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ON_RESUME", "WORKS");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause", "WORKS");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy_MAIN", "WORKS");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("!!!!!!!!!!MAIIN!!!!!!", "WORKS");

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("!!!MAIIN_RESTORE!!!!!!", "onRestoreInstanceState");
    }


}
