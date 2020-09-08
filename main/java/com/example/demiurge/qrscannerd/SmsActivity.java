package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SmsActivity extends AppCompatActivity {

    private EditText smsBody;
    private EditText smsNum;
    private Button smsGenerBtn;

    private char dividerStr = ':';

    public void encodeSMS(String BodyText, String NumText) throws UnsupportedEncodingException {
        String smsUri = "smsto:" + Uri.encode(NumText) +
                dividerStr + Uri.encode(BodyText);
        Uri encodedUriSms = Uri.parse(smsUri);

        String encodedSms = encodedUriSms.toString();

        Log.d("SMS_LOG", encodedSms);

        String resultEncoding = URLDecoder.decode(encodedSms, "UTF-8");

        Log.d("EMAIL_ENC_UTF", resultEncoding);

        Intent sendSms = new Intent(SmsActivity.this, EncodingActivity.class);
        sendSms.putExtra("qrencode", resultEncoding);
        startActivity(sendSms);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SMS_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        //TODO: add toolbar

        smsGenerBtn = (Button) findViewById(R.id.gener_sms_button);


        smsGenerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsBody = (EditText) findViewById(R.id.sms_body);
                smsNum = (EditText) findViewById(R.id.phone_num_sms);

                String smsBodyStr = smsBody.getText().toString();
                String smsNumStr = smsNum.getText().toString();

                Log.d("ON_CLICK", smsBodyStr);

                try {
                    encodeSMS(smsBodyStr, smsNumStr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}
