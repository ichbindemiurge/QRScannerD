package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LinkActivity extends AppCompatActivity {



    public void createBarcodeBitmap(String typedLink, int width, int height) throws WriterException {



        String encodedInput = Uri.encode(typedLink);


        Intent encodedLink = new Intent(LinkActivity.this, EncodingActivity.class);
        encodedLink.putExtra("qrencode",encodedInput);
        startActivity(encodedLink);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LINK_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.link_back_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);



        Button generButton = (Button) findViewById(R.id.gener_button);

        generButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText userInput = (EditText) findViewById(R.id.input_Text);
                String typedLink = userInput.getText().toString().trim();

                try {
                    createBarcodeBitmap(typedLink, 200, 200);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
