package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.net.MailTo;
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
import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class EmailActivity extends AppCompatActivity {



   public void encodeEmail (String name, String subject, String body) throws UnsupportedEncodingException {

     String uriText = "mailto:" + Uri.encode(name) +
               "?subject=" + Uri.encode(subject) +
               "&body=" + Uri.encode(body);
       Uri uriEmailEnc = Uri.parse(uriText);

       String stringU = uriEmailEnc.toString();

       Log.d("EMAIL_LOG", stringU);

       String resultEncoding = URLDecoder.decode(stringU, "UTF-8");

       Log.d("EMAIL_ENC_UTF", resultEncoding);

       Intent sendEmail = new Intent(EmailActivity.this, EncodingActivity.class);
       sendEmail.putExtra("qrencode", resultEncoding);
       startActivity(sendEmail);
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("EMAIL_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.email_activity_back_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);

        Button emailButton = (Button) findViewById(R.id.gener_email_button);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText recipientText = (EditText) findViewById(R.id.towhoText);
                EditText subjectText = (EditText) findViewById(R.id.subjectText);
                EditText bodyText = (EditText) findViewById(R.id.bodyText);

                String mRecipText = recipientText.getText().toString();
                String mSubjectText = subjectText.getText().toString();
                String mBodyText = bodyText.getText().toString();

                Log.d("ON_CLICK", mRecipText);

                try {
                    encodeEmail(mRecipText, mSubjectText, mBodyText);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
