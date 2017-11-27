package com.example.jeff.jeff23andme;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class LoginActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "0637825256de4d9e9c969ec594b032c8";
    public static final String REDIRECT_URI = "https://www.23andme.com";

    //jeff
//    public static final String CLIENT_ID = "623e75ff78854015b3b3a6c200b96bb1";
//    public static final String REDIRECT_URI = "https://www.23andme.com";

    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Logger.addLogAdapter(new AndroidLogAdapter());

        String token = Utils.getToken(this);
        if (!TextUtils.isEmpty(token)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        mWebview = new WebView(this);

        WebSettings mWebSettings = mWebview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(LoginActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url != null && url.length() >= REDIRECT_URI.length()) {
                    String urlBeginning = url.substring(0, REDIRECT_URI.length());
                    if (urlBeginning.equalsIgnoreCase(REDIRECT_URI)) {
                        //Logger.d("call back is " + urlBeginning);
                        String[] parts = url.split("=");
                        String token = parts[1]; // 034556
                        Utils.setToken(LoginActivity.this, token);
                        Logger.d("onPageFinished() called with: token = [" + token + "]");
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
            }
        });


        String url = "https://api.instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=token";
        // Logger.d("url is  " + url);

        mWebview.loadUrl(url);
        setContentView(mWebview);
    }
}
