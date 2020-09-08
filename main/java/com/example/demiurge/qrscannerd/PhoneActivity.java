package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneActivity extends AppCompatActivity {

    private EditText phoneNumEditText;
    private Button genBtn;

    public void encodePhoneNum(String phoneN)
    {
        if(!phoneN.isEmpty()) {
            String phoneUri = "tel:" + Uri.encode(phoneN);

            Uri encodedUriNum = Uri.parse(phoneUri);

            String encodedNum = encodedUriNum.toString();

            Log.d("Phone_N_LOG", encodedNum);

            Intent sendPhoneN = new Intent(PhoneActivity.this, EncodingActivity.class);
            sendPhoneN.putExtra("qrencode", encodedNum);
            startActivity(sendPhoneN);
        }
        else
        {
            //TODO: alert dialog
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        //TODO: add toolbar
        //TODO: add thumb to ImageAdapter and put it on GenerActivity menu screen

        genBtn = (Button) findViewById(R.id.gener_phone_button);

        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumEditText = (EditText) findViewById(R.id.phoneNText);

                if(!phoneNumEditText.getText().toString().isEmpty()) {
                    String phoneNumStr = phoneNumEditText.getText().toString();

                    encodePhoneNum(phoneNumStr);
                }
                else
                {
                    //TODO: alert dialog
                }
            }
        });

    }
}
