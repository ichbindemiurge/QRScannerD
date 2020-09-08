package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.WriterException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class MeCardActivity extends AppCompatActivity {

    private Button mecard;
    private TextInputEditText nameText;
    private TextInputEditText phoneText;
    private TextInputEditText companyText;
    private TextInputEditText emailText;
    private TextInputEditText addressText;

    public String sNameText;
    private boolean flag = false;

    private TextInputLayout compError;
    private TextInputLayout emailError;
    private TextInputLayout addressError;
    public TextInputLayout nameError;
    private TextInputLayout phoneError;


    private static final char separ = ';';

    private void createMeCardBarcode(String nameText, String phoneText, String companyText, String emailText, String addressText) throws WriterException, UnsupportedEncodingException {

         StringBuilder meCardString = new StringBuilder();
        String nameSign = "N:";
        String phoneSign = "TEL:";
        String companySign = "ORG:";
        String emailSign = "EMAIL:";
        String addressSign = "ADR:";
        meCardString.append("MECARD:");

        meCardString.append(nameSign).append(nameText).append(separ).append(companySign).append(companyText).append(separ)
                .append(phoneSign).append(phoneText).append(separ).append(emailSign).append(emailText).append(separ)
                .append(addressSign).append(addressText)
                .append(separ).append(separ);
        String completedCard = meCardString.toString();

        String resultEncoding = URLDecoder.decode(completedCard, "UTF-8");

        Log.d("EMAIL_ENC_UTF", resultEncoding);


        Intent encodedLink = new Intent(MeCardActivity.this, EncodingActivity.class);
        encodedLink.putExtra("qrencode",resultEncoding);
        startActivity(encodedLink);


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MECARD_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_card);

        mecard = (Button) findViewById(R.id.gener_mecard_button);


        nameText = (TextInputEditText) findViewById(R.id.nameText);
        phoneText = (TextInputEditText) findViewById(R.id.phoneText);
        companyText = (TextInputEditText) findViewById(R.id.companyText);
        emailText = (TextInputEditText) findViewById(R.id.emailText);
        addressText = (TextInputEditText) findViewById(R.id.addressText);


        nameError = (TextInputLayout) findViewById(R.id.name_error_input);
        phoneError = (TextInputLayout)  findViewById(R.id.phone_error_input);
        compError  = (TextInputLayout) findViewById(R.id.comp_error_input);
        emailError = (TextInputLayout) findViewById(R.id.email_error_input);
        addressError = (TextInputLayout) findViewById(R.id.address_error_input);



        nameText.addTextChangedListener(new CustomTextWatcher(nameText, nameError, mecard));
        phoneText.addTextChangedListener(new CustomTextWatcher(phoneText, phoneError, mecard));
        emailText.addTextChangedListener(new CustomTextWatcher(emailText, emailError, mecard));





        if(savedInstanceState != null)
        {
            MeCardActivity.this.onRestoreInstanceState(savedInstanceState);

        }else{
            Log.d("SSSSSSSSSSSSSSSSSS", "MECARD");

        }



        mecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sNameText = nameText.getText().toString().trim();
                String sPhoneText = phoneText.getText().toString().trim();
                String sCompanyText = companyText.getText().toString().trim();
                String sEmailText = emailText.getText().toString().trim();
                String sAddressText = addressText.getText().toString().trim();

                if(!sNameText.isEmpty()) {
                    try {
                        createMeCardBarcode(sNameText, sPhoneText, sCompanyText, sEmailText, sAddressText);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    mecard.setEnabled(false);
                    nameError.setError("Name field can't be empty");
                }

                System.out.println(sNameText + sPhoneText + "dfskfks");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("MECARD_ACTIVITY", "RESUME");

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("MECARD_ACTIVITY", "POST_RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MECARD_ACTIVITY", "ON_PAUSE");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MECARD_ACTIVITY", "ON_DESTROY");
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("!!!!!!!!!!MECARD!!!!!!", "onSaveInstanceState");

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("!!!MECARD!!!!!!", "onRestoreInstanceState");
    }
}
