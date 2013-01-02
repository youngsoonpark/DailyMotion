package com.rejasupotaro.dailymotion.ui.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.model.AnimationEntity;
import com.rejasupotaro.dailymotion.utils.CloseableUtils;

public class DailyMotionActivityHelper extends AbstractActivityHelper {

    private static final String TAG = DailyMotionActivityHelper.class.getSimpleName();
    private static final String EXTRA_STREAM = "android.intent.extra.STREAM";
    private static final int DEFAULT_MAX_IMAGE_SIZE = 600;
    public static final int REQUEST_GALLERY = 0;

    public void launchGallarey() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent, REQUEST_GALLERY);
    }

    public void launchActivity(Class targetClass) {
        getActivity().startActivity(new Intent(getActivity(), targetClass));
    }

    public AnimationEntity loadImageFromIntent(Intent intent) {
        AnimationEntity animationImageList = new AnimationEntity();
        if (intent == null) return animationImageList;

        ContentResolver contentResolver = getContext().getContentResolver();
        if (intent.hasExtra(EXTRA_STREAM)) {
            List<Uri> imageUriList = new ArrayList<Uri>();
            String action = intent.getAction();
            if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
                imageUriList = (List<Uri>) intent.getExtras().get(EXTRA_STREAM);
            } else if (Intent.ACTION_SEND.equals(action)) {
                imageUriList.add((Uri) intent.getExtras().get(EXTRA_STREAM));
            }
            if (imageUriList.size() > 0) {
                for (Uri imageUri: imageUriList) {
                    try {
                        Bitmap bitmap = Media.getBitmap(contentResolver, imageUri);
                        animationImageList.add(imageUri, bitmap);
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
                Uri imageUri = intent.getData();
                if (imageUri == null) return animationImageList;
                inputStream = contentResolver.openInputStream(imageUri);
                animationImageList.add(imageUri, BitmapFactory.decodeStream(inputStream));
            } catch (IOException e) {
                Log.e(TAG, "Decode failed", e);
            } finally {
                CloseableUtils.close(inputStream);
            }
        }

        return animationImageList;
    }

    public void setupSplashAnimation(final Handler handler) {
        new Thread(new Runnable() {

            private static final int START_ANIMATION = 1000;
            private static final int RUN_INTERVAL = 50;
            private int tick = 0;
            private int transparent = 255;
            private View splashView = getActivity().findViewById(R.id.view_splash);
            private ImageView splashImageView = (ImageView) getActivity().findViewById(R.id.view_splash_image);
            private TextView splashTextView = (TextView) getActivity().findViewById(R.id.view_splash_text);

            public void run() {
                while (transparent >= 0) {
                    try {
                        Thread.sleep(RUN_INTERVAL);
                    } catch (InterruptedException e) {
                        Log.v(TAG, "Something wrong with TimelineActivity", e);
                    }

                    if (tick >= START_ANIMATION) {
                        handler.post(new Runnable() {
                            public void run() {
                                transparent -= 18;

                                splashView.setBackgroundColor(Color.argb(transparent, 255, 255, 255));
                                splashImageView.setAlpha(transparent);
                                splashTextView.setTextColor(Color.argb(transparent, 120, 120, 120));

                                if (transparent <= 0) {
                                    splashView.setVisibility(View.GONE);
                                    splashImageView.setVisibility(View.GONE);
                                    splashTextView.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                    tick += RUN_INTERVAL;
                }
            }
        }).start();
    }
}
