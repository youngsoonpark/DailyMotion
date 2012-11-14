package com.rejasupotaro.dailymotion.ui;

import com.rejasupotaro.dailymotion.Constants;
import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.R.id;
import com.rejasupotaro.dailymotion.R.layout;
import com.rejasupotaro.dailymotion.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TimelineActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        WebView timelineWebView = (WebView) findViewById(R.id.webview_timeline);
        timelineWebView.getSettings().setJavaScriptEnabled(true);
        timelineWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                setProgress(newProgress * 100);
            }
        });
        timelineWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        timelineWebView.loadUrl(Constants.APP_SITE_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
