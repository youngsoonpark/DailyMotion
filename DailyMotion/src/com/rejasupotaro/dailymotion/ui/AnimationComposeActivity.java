package com.rejasupotaro.dailymotion.ui;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.api.DailyMotionApiClient;
import com.rejasupotaro.dailymotion.model.AnimationEntity;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionHelper;
import com.rejasupotaro.dailymotion.utils.ToastUtils;

public class AnimationComposeActivity extends FragmentActivity implements LoaderCallbacks<StatusLine> {

    private static final String TAG = AnimationComposeActivity.class.getSimpleName();
    private static final int REQUEST_UPLOAD = 1;

    private DailyMotionHelper mDailyMotionHelper;
    private AnimationView mAnimationView;
    private AnimationEntity mAnimationEntity;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDailyMotionHelper = new DailyMotionHelper(this);

        mAnimationView = (AnimationView) findViewById(R.id.image_animation_view);
        mAnimationView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mAnimationEntity.size() == 0) {
                    mDailyMotionHelper.launchGallarey();
                }
            }
        });

        Button postButton = (Button) findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportLoaderManager().initLoader(
                        REQUEST_UPLOAD, null, AnimationComposeActivity.this);
            }
        });

        Button closeButton = (Button) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDailyMotionHelper.launchActivity(TimelineActivity.class);
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

        mAnimationEntity = mDailyMotionHelper.loadImageFromIntent(getIntent());
        if (mAnimationEntity.size() > 0) {
            mAnimationView.setupAnimation(mAnimationEntity.getBitmapList());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == DailyMotionHelper.REQUEST_GALLERY && resultCode == RESULT_OK) {
            mAnimationEntity = mDailyMotionHelper.loadImageFromIntent(getIntent());
            if (mAnimationEntity.size() > 0) {
                mAnimationView.setImageBitmap(mAnimationEntity.getBitmap(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public Loader<StatusLine> onCreateLoader(int id, Bundle args) {
        switch (id) {
        case REQUEST_UPLOAD:
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.progress_uploading));
            mProgressDialog.show();

            mAnimationEntity.setTitle(((EditText) findViewById(R.id.edit_text_image_title)).getText().toString());
            mAnimationEntity.setDelay(mAnimationView.getDelay());
            return new DailyMotionApiClient(this, mAnimationEntity);
        default:
            Log.v(TAG, "Can't create AsyncTaskLoader. Undefined id: " + id);
            return null;
        }
    }

    public void onLoaderReset(Loader<StatusLine> loader) {
    }

    public void onLoadFinished(Loader<StatusLine> loader, StatusLine result) {
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;

        if (result.getStatusCode() == HttpStatus.SC_OK) {
            ToastUtils.show(this, R.string.upload_completed);
            getSupportLoaderManager().destroyLoader(loader.getId());
        } else {
            ToastUtils.show(this, R.string.upload_failed);
        }
    }
}
