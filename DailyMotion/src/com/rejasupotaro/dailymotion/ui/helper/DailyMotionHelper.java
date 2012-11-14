package com.rejasupotaro.dailymotion.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class DailyMotionHelper {
    public static final int REQUEST_GALLERY = 0;
    
    public static void launchGallarey(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, REQUEST_GALLERY);
    }
    
    public static void launchTimelineActivity(Context context, Class targetClass) {
        context.startActivity(new Intent(context, targetClass));
    }
}
