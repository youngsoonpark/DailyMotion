package com.rejasupotaro.dailymotion.utils;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.R.string;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    
    public static void show(Context context, int resId) {
        Toast.makeText(context, context.getString(R.string.upload_completed), Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
