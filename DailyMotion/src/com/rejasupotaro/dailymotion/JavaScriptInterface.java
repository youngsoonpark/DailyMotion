package com.rejasupotaro.dailymotion;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.webkit.WebView;

public class JavaScriptInterface {
    private static final String TAG = JavaScriptInterface.class.getSimpleName();

    private static final String INTERFACE_NAME = "Device";

    private WebView mWebView;
    private JavaScriptInterface.Receiver mReceiver;

    public JavaScriptInterface(WebView webView, Receiver receiver) {
        mWebView = webView;
        mReceiver = receiver;

        mWebView.addJavascriptInterface(this, INTERFACE_NAME);
    }

    public void call(String data) {
        if (mReceiver == null) return;

        try {
            mReceiver.receive(new JSONObject(data));
        } catch (JSONException e) {
            Log.e(TAG, data, e);
        }
    }

    public void callBrowserMethod(String jsMethodName) {
        mWebView.loadUrl("javascript:" + jsMethodName + "()");
    }

    public interface Receiver {
        public void receive(JSONObject jsonObject);
    }
}
