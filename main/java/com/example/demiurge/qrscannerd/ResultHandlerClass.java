package com.example.demiurge.qrscannerd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.WifiParsedResult;

/**
 * Created by demiurge on 28.04.18.
 */


public class ResultHandlerClass {

    Context context;
    private String TAG = this.getClass().getSimpleName();


    public ResultHandlerClass(Context context)
    {
        this.context = context;
    }

    public void sendPhoneN(ParsedResult parsedResult)
    {
        PhoneResultHandlerActivity phoneResultHandlerActivity = new PhoneResultHandlerActivity();
        Log.d("RESULT_HANDLER", "TYPE: " + parsedResult.getType().toString());
        phoneResultHandlerActivity.setParsedResult(parsedResult);
        Intent i = new Intent();
        i.setClassName("com.example.demiurge.qrscannerd", "com.example.demiurge.qrscannerd.PhoneResultHandlerActivity");
        context.startActivity(i);
        //phoneResultHandlerActivity.fillPhoneTextView(parsedResult);
    }

    public void sendEmail(ParsedResult parsedResult)
    {
        EMailResultHandlerActivity eMailResultHandlerActivity = new EMailResultHandlerActivity();
        eMailResultHandlerActivity.setParsedResult(parsedResult);
        Intent emailResultSend = new Intent();
        emailResultSend.setClassName("com.example.demiurge.qrscannerd", "com.example.demiurge.qrscannerd.EMailResultHandlerActivity");
        context.startActivity(emailResultSend);
    }

    public void addMeCard(ParsedResult parsedResult)
    {
        MeCardResultHandlerActivity meCardResultHandlerActivity = new MeCardResultHandlerActivity();
        meCardResultHandlerActivity.setParsedResult(parsedResult);
        Intent mecardAddContacts  = new Intent();
        mecardAddContacts.setClassName("com.example.demiurge.qrscannerd", "com.example.demiurge.qrscannerd.MeCardResultHandlerActivity");
        context.startActivity(mecardAddContacts);
    }


    public void showTextResult(ParsedResult parsedResult)
    {
        TextParsedResult text = (TextParsedResult) parsedResult;
        Log.d(TAG, text.getText());

        TextResultHandlerActivity textResultHandlerActivity = new TextResultHandlerActivity();
        textResultHandlerActivity.setParsedResult(parsedResult);
        Intent openTextRes = new Intent();
        openTextRes.setClassName("com.example.demiurge.qrscannerd", "com.example.demiurge.qrscannerd.TextResultHandlerActivity");
        context.startActivity(openTextRes);
    }

    public void showWIFIInfo(ParsedResult parsedResult)
    {
        WifiParsedResult wifiParsedResult = (WifiParsedResult) parsedResult;
        Log.d(TAG, wifiParsedResult.toString());

        WIFIResultHandlerActivity wifiResultHandlerActivity = new WIFIResultHandlerActivity();
        wifiResultHandlerActivity.setParsedResult(parsedResult);
        Intent openWifiRes = new Intent();
        openWifiRes.setClassName("com.example.demiurge.qrscannerd", "com.example.demiurge.qrscannerd.WIFIResultHandlerActivity");
        context.startActivity(openWifiRes);
    }


}
