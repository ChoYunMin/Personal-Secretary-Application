package com.example.nawoo.secretaryproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dbsal on 2017-12-18.
 */

public class SetTrafficActivity extends AppCompatActivity{

    private WebView mWebView;
    private WebSettings mWebSettings;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_traffic);

        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://211.214.113.144:8888/Dproject/map.php");
    }
}
