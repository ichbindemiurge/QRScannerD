package com.example.demiurge.qrscannerd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ViewfinderView;

public class WebViewActivity extends AppCompatActivity {


    private final String BUNDLE_KEY = "WEB_VIEW_SAVE_STATE";

    private WebView webViewBrowser;
    public Intent toWebViewData;
    final Activity activity = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("WEB_VIEW_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_web_view);



        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.web_view_back_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);


        webViewBrowser = (WebView) findViewById(R.id.web_view_browser); // set the entire activity window as webview

        toWebViewData = getIntent();
        String uriString = toWebViewData.getStringExtra("URI");
        Log.d("DATA_OUTPUT", uriString);


        webViewBrowser.getSettings().setJavaScriptEnabled(true);

        webViewBrowser.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               setProgress(newProgress * 100);

                Log.d("Progress_bar", "Called");
            }

        });



        webViewBrowser.setWebViewClient(new webViewClient());

        webViewBrowser.loadUrl(uriString);



        if(savedInstanceState != null)
        {
            webViewBrowser.restoreState(savedInstanceState.getBundle(BUNDLE_KEY));
        }
        else
        {
            webViewBrowser.loadUrl(uriString);
        }


    }



    class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {

        webViewBrowser.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webViewBrowser.restoreState(savedInstanceState);
    }

}
