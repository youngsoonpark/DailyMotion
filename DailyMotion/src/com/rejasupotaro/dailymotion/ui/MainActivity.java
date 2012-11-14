package com.rejasupotaro.dailymotion.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionHelper;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DailyMotionHelper mDailyMotionHelper;
    private ImageView mAnimationView;
    private List<Bitmap> mBitmapList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDailyMotionHelper = new DailyMotionHelper(this);

        mAnimationView = (ImageView) findViewById(R.id.image_animation_view);
        mAnimationView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDailyMotionHelper.launchGallarey();
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
                mDailyMotionHelper.launchActivity(TimelineActivity.class);
            }
        });

        mBitmapList = mDailyMotionHelper.loadBitmapFromIntent(getIntent());
        if (mBitmapList.size() > 0) {
            mAnimationView.setImageBitmap(mBitmapList.get(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == DailyMotionHelper.REQUEST_GALLERY && resultCode == RESULT_OK) {
            mBitmapList = mDailyMotionHelper.loadBitmapFromIntent(getIntent());
            if (mBitmapList.size() > 0) {
                mAnimationView.setImageBitmap(mBitmapList.get(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
