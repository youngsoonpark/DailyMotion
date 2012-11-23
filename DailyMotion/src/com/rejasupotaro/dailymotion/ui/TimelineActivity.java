package com.rejasupotaro.dailymotion.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;

import com.rejasupotaro.dailymotion.Constants;
import com.rejasupotaro.dailymotion.R;

public class TimelineActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        WebView timelineWebView = (WebView) findViewById(R.id.webview_timeline);
        timelineWebView.getSettings().setJavaScriptEnabled(true);
        setupWebViewClient(timelineWebView);
        timelineWebView.clearCache(true);
        timelineWebView.loadUrl(Constants.APP_SITE_URL);
    }

    private void setupWebViewClient(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {});

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingProgress();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setPictureListener(new PictureListener() {
            public void onNewPicture(WebView view, Picture picture) {
                hideLoadingProgress();
            }
        });
    }

    private void showLoadingProgress() {
        findViewById(R.id.progress_loading).setVisibility(View.VISIBLE);
        findViewById(R.id.webview_timeline).setVisibility(View.GONE);
    }

    private void hideLoadingProgress() {
        findViewById(R.id.progress_loading).setVisibility(View.GONE);
        findViewById(R.id.webview_timeline).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
