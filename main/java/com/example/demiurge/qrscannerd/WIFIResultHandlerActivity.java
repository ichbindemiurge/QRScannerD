package com.example.demiurge.qrscannerd;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.WifiParsedResult;
import com.google.zxing.client.result.WifiResultParser;

import java.util.regex.Pattern;

public class WIFIResultHandlerActivity extends AppCompatActivity {

    private TextView wifiSSID;
    private TextView wifiPassword;
    private Button wifiConnectBtn;

    private static String TAG = WIFIResultHandlerActivity.class.getSimpleName();
    private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");



    private static ParsedResult parsedResult;


    public WIFIResultHandlerActivity() {

    }

    public void setParsedResult(ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifiresult_handler);


        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.wifi_res_app_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        wifiSSID = (TextView) findViewById(R.id.wifi_res_ssis);
        wifiPassword = (TextView) findViewById(R.id.wifi_res_password);
        wifiConnectBtn = (Button) findViewById(R.id.wifi_res_gen_but);


        fillInWifiInfo();

        wifiConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WifiConfigManager();

            }
        });


    }

    public void fillInWifiInfo() {
        WifiParsedResult wifiParsedResult = (WifiParsedResult) parsedResult;
        Log.d(TAG, parsedResult.toString());

        String ssid = wifiParsedResult.getSsid();
        String password = wifiParsedResult.getPassword();
        String encMode = wifiParsedResult.getNetworkEncryption();

        wifiSSID.setText(ssid);
        wifiPassword.setText(password);
        //wifiEncMode.setText(encMode);


    }

    public void WifiConfigManager() {

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiParsedResult wifiParsedResult = (WifiParsedResult) parsedResult;
            Log.d(TAG, parsedResult.toString());


            String networkType = wifiParsedResult.getNetworkEncryption();
            String password = wifiParsedResult.getPassword();

            if(password != null && !password.isEmpty())
            {
                switch (networkType)
                {
                    case "WEP":
                        changeNetworkWEP(wifiManager, wifiParsedResult);
                        break;
                    case "WPA":
                        changeNetworkWPA(wifiManager, wifiParsedResult);
                        break;
                    case "":
                        changeNetworkUnEncrypted(wifiManager, wifiParsedResult);
                        break;
                }
            }

        }



    private static WifiConfiguration changeNetworkCommon(WifiParsedResult wifiResult) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = quoteNonHex(wifiResult.getSsid());
        config.hiddenSSID = wifiResult.isHidden();
        return config;
    }

    // Adding a WEP network
    private static void changeNetworkWEP(WifiManager wifiManager, WifiParsedResult wifiResult) {
        WifiConfiguration config = changeNetworkCommon(wifiResult);
        config.wepKeys[0] = quoteNonHex(wifiResult.getPassword(), 10, 26, 58);
        config.wepTxKeyIndex = 0;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        updateNetwork(wifiManager, config);
    }

    private static void changeNetworkWPA(WifiManager wifiManager, WifiParsedResult wifiResult) {
        WifiConfiguration config = changeNetworkCommon(wifiResult);
        config.preSharedKey = quoteNonHex(wifiResult.getPassword(), 64);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        updateNetwork(wifiManager, config);
    }



    private static void changeNetworkUnEncrypted(WifiManager wifiManager, WifiParsedResult wifiResult) {
        WifiConfiguration config = changeNetworkCommon(wifiResult);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        updateNetwork(wifiManager, config);
    }

    private static String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    }


    private static String convertToQuotedString(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s;
        }
        return '\"' + s + '\"';
    }

    private static boolean isHexOfLength(CharSequence value, int... allowedLengths) {
        if (value == null /* || !matcher(value).matches()*/) {
            return false;
        }
        if (allowedLengths.length == 0) {
            return true;
        }
        for (int length : allowedLengths) {
            if (value.length() == length) {
                return true;
            }
        }
        return false;
    }



    private static void updateNetwork(WifiManager wifiManager, WifiConfiguration config) {
     /*   Integer foundNetworkID = findNetworkInExistingConfig(wifiManager, config.SSID);
        if (foundNetworkID != null) {
            Log.i(TAG, "Removing old configuration for network " + config.SSID);
            wifiManager.removeNetwork(foundNetworkID);
            wifiManager.saveConfiguration();
        }*/
        int networkId = wifiManager.addNetwork(config);
        if (networkId >= 0) {
            if (wifiManager.enableNetwork(networkId, true)) {
                Log.i(TAG, "Associating to network " + config.SSID);
                wifiManager.saveConfiguration();
            } else {
                Log.w(TAG, "Failed to enable network " + config.SSID);
            }
        } else {
            Log.w(TAG, "Unable to add network " + config.SSID);
        }
    }

    private static Integer findNetworkInExistingConfig(WifiManager wifiManager, String ssid) {
        Iterable<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                String existingSSID = existingConfig.SSID;
                if (existingSSID != null && existingSSID.equals(ssid)) {
                    return existingConfig.networkId;
                }
            }
        }
        return null;
    }




}


