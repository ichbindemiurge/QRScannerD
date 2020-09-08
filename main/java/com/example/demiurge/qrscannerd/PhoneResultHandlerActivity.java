package com.example.demiurge.qrscannerd;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.TelParsedResult;

public class PhoneResultHandlerActivity extends AppCompatActivity {

    private TextView phoneTextView;
    private Button callButton;
    private String blank = "1";

    public static ParsedResult parsedResult;


    public PhoneResultHandlerActivity()
    {

    }

    PhoneResultHandlerActivity(ParsedResult parsedResult)
    {
        this.parsedResult = parsedResult;
        Log.d("Params", "Called");
        Log.d("Params", parsedResult.toString());
    }


    public ParsedResult getParsedResult() {
        Log.d("Getter", parsedResult.toString());
        return parsedResult;
    }

    public void setParsedResult(ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_result_handler);


        Log.d("OnCreate", parsedResult.toString());

        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        callButton = (Button) findViewById(R.id.call_button);

        phoneTextView.setText(fillPhoneTextView());


        Log.d("HANDLER", "YES");


    }

    public String fillPhoneTextView()
    {
        TelParsedResult telParsedResult = (TelParsedResult) parsedResult;

        String phoneNum = telParsedResult.getNumber();

        return phoneNum;
    }
}
