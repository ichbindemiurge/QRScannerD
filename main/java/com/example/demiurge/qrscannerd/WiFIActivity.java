package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WiFIActivity extends AppCompatActivity {

    EditText wifiSSID;
    EditText wifiPassword;
    Button wifiGenButton;
    CheckBox isHidden;
    RadioGroup wifiEncryptionMode;

    String hidden = "";
    String ssidHidden = "";
    String encMode = "";


    private String separator = ";";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.wifi_act_app_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        wifiSSID = (EditText) findViewById(R.id.wifi_act_ssis);
        wifiPassword = (EditText) findViewById(R.id.wifi_act_password);
        wifiGenButton = (Button) findViewById(R.id.wifi_act_gen_but);
        isHidden = (CheckBox) findViewById(R.id.wifi_act_check_hidden_wifi);
        wifiEncryptionMode = (RadioGroup) findViewById(R.id.wifi_act_radio_group);


        wifiGenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sWifiName = wifiSSID.getText().toString().trim();
                String sWifiPass = wifiPassword.getText().toString().trim();
                String wifiMode = radioChoice();

                System.out.println("AAAAAAAAAAAAAAAAAAA" + wifiMode);

                try {
                    encodeWiFI(sWifiName, sWifiPass, wifiMode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    public void encodeWiFI(String wifiName, String wifiPas, String radioChoosen) throws UnsupportedEncodingException {

        StringBuilder wifiParsedString = new StringBuilder();


        String wifi = "WIFI:";
        String authentication = "T:";
        String networkSsid = "S:";
        String networkPassword = "P:";

        if (isHidden.isChecked())
        {
            hidden = "H:";
            ssidHidden = "true;";
        }
        else
        {
            hidden = "";
            ssidHidden = "";
        }



        wifiParsedString.append(wifi).append(networkSsid).append(wifiName).append(separator).append(authentication)
                .append(radioChoosen).append(separator).append(networkPassword).append(wifiPas).append(separator)
                .append(hidden).append(ssidHidden).append(separator);

        String completedWifiEncoding = wifiParsedString.toString();


        String resultEncoding = URLDecoder.decode(completedWifiEncoding, "UTF-8");

        Log.d("WIFI_ENC_UTF", resultEncoding);


        Intent encodedWIFI = new Intent(WiFIActivity.this, EncodingActivity.class);
        encodedWIFI.putExtra("qrencode", resultEncoding);
        startActivity(encodedWIFI);



    }



    public String radioChoice()
    {
        int radioSelectedID = wifiEncryptionMode.getCheckedRadioButtonId();
        String radioChoosen = "";
        switch (radioSelectedID)
        {
            case R.id.wifi_act_radioButton_wep:
                radioChoosen = "WEP";
                break;
            case R.id.wifi_act_radioButton_wpa:
                radioChoosen = "WPA";
                break;
            case R.id.wifi_act_radioButton_no_encryption:
                radioChoosen = "";
                break;
        }
        Log.d("RADIO", radioChoosen);

        return radioChoosen;
    }

}
