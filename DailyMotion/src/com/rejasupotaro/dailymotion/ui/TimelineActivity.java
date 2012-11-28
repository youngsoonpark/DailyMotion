package com.rejasupotaro.dailymotion.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;

import com.rejasupotaro.dailymotion.Constants;
import com.rejasupotaro.dailymotion.JavaScriptInterface;
import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.utils.ToastUtils;
import com.rejasupotaro.dailymotion.utils.UriUtils;

public class TimelineActivity extends Activity {
    private static final String TAG = TimelineActivity.class.getSimpleName();

    private JavaScriptInterface mJavaScriptInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        WebView timelineWebView = (WebView) findViewById(R.id.webview_timeline);
        timelineWebView.getSettings().setJavaScriptEnabled(true);
        setupWebViewClient(timelineWebView);
        timelineWebView.clearCache(false);
        timelineWebView.loadUrl(Constants.APP_SITE_URL);

        mJavaScriptInterface = new JavaScriptInterface(this, timelineWebView);
        mJavaScriptInterface.setOnCallFromBrowser(new JavaScriptInterface.Receiver() {
            public void receive(JSONObject jsonObject) {
                try {
                    JSONObject body = jsonObject.getJSONObject("body");
                    Log.d(TAG, body.toString());
                    if (body.has("title")) {
                        String title = body.getString("title");
                        ToastUtils.show(TimelineActivity.this, title + "にLikeしました");
                    } else if (body.has("image_url")) {
                        String image_url = body.getString("image_url");
                        ToastUtils.show(TimelineActivity.this, image_url);
                    } else {
                        Log.v(TAG, "Received unknown body. Check your code.");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, jsonObject.toString(), e);
                }
            }
        });
    }

    private void setupWebViewClient(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {});

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Uri uri = Uri.parse(url);
                if (Constants.PRODUCTION && !UriUtils.compareDomain(uri, Constants.APP_SITE_URL)) {
                    throw new SecurityException();
                }

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
