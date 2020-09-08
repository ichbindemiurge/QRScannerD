package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;

public class EMailResultHandlerActivity extends AppCompatActivity {

    private TextView recipientTextView;
    private TextView subjectTextView;
    private TextView bodyTextView;
    private Button sendButton;


    public static ParsedResult parsedResult;
    public String emailToStr;


    public void setParsedResult(ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
        Log.d("SETTER", parsedResult.toString());
    }

    public ParsedResult getParsedResult() {
        return parsedResult;
    }


    public EMailResultHandlerActivity()
    {

    }

    EMailResultHandlerActivity(ParsedResult parsedResult)
    {
        this.parsedResult = parsedResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_result_handler);

        recipientTextView = (TextView) findViewById(R.id.recipient_email);
        subjectTextView = (TextView) findViewById(R.id.subject_email);
        bodyTextView = (TextView) findViewById(R.id.body_email);
        sendButton = (Button) findViewById(R.id.send_email_button);

        fillInEmailFields();


    }

    public void fillInEmailFields()
    {
        EmailAddressParsedResult email = (EmailAddressParsedResult) parsedResult;
        Log.d("FillEmail", parsedResult.toString());

        Log.d("FillEmail", "E-mail: " + email.getTos()[0] + " Subject: " + email.getSubject() + " Body: " + email.getBody());

        emailToStr = email.getTos()[0]; //only one recipient for the email
        Intent emailOpener = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailToStr));
        emailOpener.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());
        emailOpener.putExtra(Intent.EXTRA_TEXT, email.getBody());

        recipientTextView.setText(emailToStr);
        subjectTextView.setText(email.getSubject());
        bodyTextView.setText(email.getBody());

    }
}
