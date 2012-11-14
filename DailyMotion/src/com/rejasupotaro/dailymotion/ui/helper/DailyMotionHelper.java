package com.rejasupotaro.dailymotion.ui.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.rejasupotaro.dailymotion.DailyMotionUtils;

public class DailyMotionHelper {
    
    private static final String TAG = DailyMotionHelper.class.getSimpleName();
    private static final String EXTRA_STREAM = "android.intent.extra.STREAM";
    public static final int REQUEST_GALLERY = 0;

    private Activity mActivity;

    public DailyMotionHelper(Activity activity) {
        mActivity = activity;
    }

    public void launchGallarey() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivity.startActivityForResult(intent, REQUEST_GALLERY);
    }

    public void launchActivity(Class targetClass) {
        mActivity.startActivity(new Intent(mActivity, targetClass));
    }

    public List<Bitmap> loadBitmapFromIntent(Intent intent) {
        List<Bitmap> bitmapList = new ArrayList<Bitmap>();
        if (intent == null) return bitmapList;

        ContentResolver contentResolver = mActivity.getContentResolver();
        if (intent.hasExtra(EXTRA_STREAM)) {
            List<Uri> imageUriList = new ArrayList<Uri>();
            try{
                String action = intent.getAction();
                if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
                    imageUriList = (List<Uri>) intent.getExtras().get(EXTRA_STREAM);
                } else if (Intent.ACTION_SEND.equals(action)) {
                    imageUriList.add((Uri) intent.getExtras().get(EXTRA_STREAM));
                }
            } catch(Exception e){
                Log.v(TAG, "Uri parse failed", e);
            }
            if (imageUriList.size() > 0) {
                for (Uri uri: imageUriList) {
                    try {
                        bitmapList.add(Media.getBitmap(contentResolver, uri));
                    } catch (FileNotFoundException e) {
                        Log.v(TAG, "Content resolve filed", e);
                    } catch (IOException e) {
                        Log.v(TAG, "Load bitmap failed", e);
                    }
                }
            }
        } else {
            InputStream inputStream = null;
            try {
                inputStream = contentResolver.openInputStream(intent.getData());
                bitmapList.add(BitmapFactory.decodeStream(inputStream));
            } catch (Exception e) {
                Log.e(TAG, "Decode failed", e);
            } finally {
                DailyMotionUtils.close(inputStream);
            }
        }

        return bitmapList;
    }
}
