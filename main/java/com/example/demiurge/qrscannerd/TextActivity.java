package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class TextActivity extends AppCompatActivity {

    private EditText textInput;
    private Button textInpGenBut;
    private String TAG = TextActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.text_act_app_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        textInpGenBut = (Button) findViewById(R.id.textinput_generate_button);



        textInpGenBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInput = (EditText) findViewById(R.id.text_input_encode);
                String textFromEdit = textInput.getText().toString();

                Log.d(TAG, "Button Pressed");

                try {
                    generateText(textFromEdit);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public void generateText(String textToEncode) throws UnsupportedEncodingException {
        Log.d(TAG, textToEncode);

        String resultEncoding = URLDecoder.decode(textToEncode, "UTF-8");

        Log.d("EMAIL_ENC_UTF", resultEncoding);

        Intent encodeText = new Intent(TextActivity.this, EncodingActivity.class);
        encodeText.putExtra("qrencode", resultEncoding);
        startActivity(encodeText);
    }
}
