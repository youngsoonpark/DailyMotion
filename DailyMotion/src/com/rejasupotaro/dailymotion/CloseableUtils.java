package com.rejasupotaro.dailymotion;

import java.io.Closeable;
import java.io.IOException;

import android.util.Log;

public class CloseableUtils {
    private static final String TAG = CloseableUtils.class.getSimpleName();

    public static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            Log.e(TAG, "An error occurred in DailyMotionpUtils.close()", e);
        }
    }
}
