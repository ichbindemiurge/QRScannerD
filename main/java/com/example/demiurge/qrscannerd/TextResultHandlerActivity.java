package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.TextParsedResult;

public class TextResultHandlerActivity extends AppCompatActivity {

    private TextView textResult;
    private String TAG = this.getClass().getSimpleName();

    public static ParsedResult parsedResult;

    private String textResStr;


    public TextResultHandlerActivity()
    {

    }


    public void setParsedResult(ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_result_handler);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.text_res_app_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        textResult = (TextView) findViewById(R.id.text_result_handler);

        fillInTextField();

    }


    public void fillInTextField()
    {
        TextParsedResult text = (TextParsedResult) parsedResult;
        Log.d(TAG, text.getText());

        textResStr = text.getText();

        textResult.setText(textResStr);
    }
}
