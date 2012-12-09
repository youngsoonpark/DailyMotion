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

    public void callBrowserMethod(String jsMethodName, String... args) {
        String jsArgs = "";
        for (int i = 0; i < args.length; i++) {
            jsArgs += args[i];
            if (i != args.length - 1) {
                jsArgs += ",";
            }
        }
        mWebView.loadUrl("javascript:" + jsMethodName + "(" + jsArgs + ")");
    }

    public interface Receiver {
        public void receive(JSONObject jsonObject);
    }
}
