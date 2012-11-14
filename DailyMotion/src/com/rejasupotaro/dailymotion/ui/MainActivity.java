package com.rejasupotaro.dailymotion.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.rejasupotaro.dailymotion.DailyMotionUtils;
import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.R.id;
import com.rejasupotaro.dailymotion.R.layout;
import com.rejasupotaro.dailymotion.R.menu;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionHelper;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_STREAM = "android.intent.extra.STREAM";

    private ImageView mAnimationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnimationView = (ImageView) findViewById(R.id.image_animation_view);
        mAnimationView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DailyMotionHelper.launchGallarey(MainActivity.this);
            }
        });

        SeekBar animationSpeedSeekBar = (SeekBar) findViewById(R.id.seekbar_animation_speed);
        animationSpeedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            // トラッキング開始時に呼び出されます
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v("onStartTrackingTouch()",
                        String.valueOf(seekBar.getProgress()));
            }
            // トラッキング中に呼び出されます
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                Log.v("onProgressChanged()",
                        String.valueOf(progress) + ", " + String.valueOf(fromTouch));
            }
            // トラッキング終了時に呼び出されます
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.v("onStopTrackingTouch()",
                        String.valueOf(seekBar.getProgress()));
            }
        });

        Button closeButton = (Button) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DailyMotionHelper.launchTimelineActivity(MainActivity.this, TimelineActivity.class);
            }
        });

        loadBitmapFromIntent(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == DailyMotionHelper.REQUEST_GALLERY && resultCode == RESULT_OK) {
            loadBitmapFromIntent(intent);
        } 
    }

    private void loadBitmapFromIntent(Intent intent) {
        if (intent == null) return;

        Bitmap bitmap = null;
        if (intent.hasExtra(EXTRA_STREAM)) {
            Uri imageUri = null;
            try{
                imageUri = Uri.parse(intent.getExtras().get(EXTRA_STREAM).toString());
            } catch(Exception e){
                Log.v(TAG, "Uri parse failed", e);
            }
            if (imageUri != null) {
                try {
                    bitmap = Media.getBitmap(getContentResolver(), imageUri);
                } catch (FileNotFoundException e) {
                    Log.v(TAG, "Content resolve filed", e);
                } catch (IOException e) {
                    Log.v(TAG, "Load bitmap failed", e);
                }
            }
        } else {
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(intent.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                mAnimationView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e(TAG, "Decode failed", e);
            } finally {
                DailyMotionUtils.close(inputStream);
            }
        }

        if (bitmap != null) {
            mAnimationView.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
