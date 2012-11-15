package com.rejasupotaro.dailymotion.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.model.AnimationImageList;
import com.rejasupotaro.dailymotion.model.DailyMotionApiClient;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionHelper;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Void> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_UPLOAD = 1;

    private DailyMotionHelper mDailyMotionHelper;
    private AnimationView mAnimationView;
    private AnimationImageList mAnimationImageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDailyMotionHelper = new DailyMotionHelper(this);

        mAnimationView = (AnimationView) findViewById(R.id.image_animation_view);
        mAnimationView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDailyMotionHelper.launchGallarey();
            }
        });

        SeekBar animationSpeedSeekBar = (SeekBar) findViewById(R.id.seekbar_animation_speed);
        animationSpeedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAnimationView.setAnimationInterval(seekBar.getProgress());
            }
        });

        Button postButton = (Button) findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportLoaderManager().initLoader(REQUEST_UPLOAD, new Bundle(), MainActivity.this);
            }
        });

        Button closeButton = (Button) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDailyMotionHelper.launchActivity(TimelineActivity.class);
            }
        });

        mAnimationImageList = mDailyMotionHelper.loadImageFromIntent(getIntent());
        if (mAnimationImageList.size() > 0) {
            mAnimationView.setupAnimation(mAnimationImageList.getBitmapList());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == DailyMotionHelper.REQUEST_GALLERY && resultCode == RESULT_OK) {
            mAnimationImageList = mDailyMotionHelper.loadImageFromIntent(getIntent());
            if (mAnimationImageList.size() > 0) {
                mAnimationView.setImageBitmap(mAnimationImageList.getBitmap(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public Loader<Void> onCreateLoader(int id, Bundle args) {
        if(args == null) return null;

        switch (id) {
        case REQUEST_UPLOAD:
            return new DailyMotionApiClient(this, mAnimationImageList.getUriList(), mAnimationImageList.getFileBodyList());
        default:
            Log.v(TAG, "Can't create AsyncTaskLoader. Undefined id: " + id);
            return null;
        }
    }

    public void onLoadFinished(Loader<Void> arg0, Void arg1) {
    }

    public void onLoaderReset(Loader<Void> arg0) {
    }
}
