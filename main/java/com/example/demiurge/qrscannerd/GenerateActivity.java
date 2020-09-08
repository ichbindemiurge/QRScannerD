package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GenerateActivity extends AppCompatActivity {



    String [] optionsQRGen = { "Link", "vCard", "Email", "SMS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("GENERATE_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);


        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.generate_back_bar);
        setSupportActionBar(toolbarHistory);


        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        GridView generateOptions = (GridView) findViewById(R.id.grid_gen_opt);
        generateOptions.setAdapter(new ImageAdapter(this));


        generateOptions.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(optionsQRGen[position].equals("Link"))
                {
                    Intent linkOpenIn = new Intent(GenerateActivity.this, LinkActivity.class);
                    startActivity(linkOpenIn);
                }
                if(optionsQRGen[position].equals("vCard"))
                {
                    Intent contactOpenIn = new Intent (GenerateActivity.this, MeCardActivity.class);
                    startActivity(contactOpenIn);
                }
                if(optionsQRGen[position].equals("Email"))
                {
                    Intent openEmail = new Intent(GenerateActivity.this, EmailActivity.class);
                    startActivity(openEmail);
                }
                if(optionsQRGen[position].equals("SMS"))
                {
                    Intent openEmail = new Intent(GenerateActivity.this, SmsActivity.class);
                    startActivity(openEmail);
                }

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GENER", "Destroy");
    }



}

