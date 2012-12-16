package com.rejasupotaro.dailymotion.ui;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.inject.Inject;
import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.api.DailyMotionApiClient;
import com.rejasupotaro.dailymotion.model.AnimationEntity;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionActivityHelper;
import com.rejasupotaro.dailymotion.utils.ToastUtils;

@ContentView(R.layout.activity_main)
public class AnimationComposeActivity extends RoboFragmentActivity implements LoaderCallbacks<StatusLine> {

    private static final String TAG = AnimationComposeActivity.class.getSimpleName();
    private static final int REQUEST_UPLOAD = 1;

    @InjectView(R.id.image_animation_view)
    private AnimationView mAnimationView;

    @InjectView(R.id.button_post)
    private Button mPostButton;

    @InjectView(R.id.button_close)
    private Button mCloseButton;

    @InjectView(R.id.seekbar_animation_speed)
    private SeekBar mAnimationSpeedSeekBar;

    @InjectView(R.id.edit_text_image_title)
    private EditText mImageTitleEditText;

    @Inject
    private DailyMotionActivityHelper mActivityHelper;

    private AnimationEntity mAnimationEntity;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnimationView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mAnimationEntity.size() == 0) {
                    mActivityHelper.launchGallarey();
                }
            }
        });

        mPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportLoaderManager().initLoader(
                        REQUEST_UPLOAD, null, AnimationComposeActivity.this);
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mActivityHelper.launchActivity(TimelineActivity.class);
            }
        });

        mAnimationSpeedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAnimationView.setAnimationInterval(seekBar.getProgress());
            }
        });

        mAnimationEntity = mActivityHelper.loadImageFromIntent(getIntent());
        if (mAnimationEntity.size() > 0) {
            mAnimationView.setupAnimation(mAnimationEntity.getBitmapList());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == DailyMotionActivityHelper.REQUEST_GALLERY && resultCode == RESULT_OK) {
            mAnimationEntity = mActivityHelper.loadImageFromIntent(getIntent());
            if (mAnimationEntity.size() > 0) {
                mAnimationView.setImageBitmap(mAnimationEntity.getBitmap(0));
            }
        }
    }

    public Loader<StatusLine> onCreateLoader(int id, Bundle args) {
        switch (id) {
        case REQUEST_UPLOAD:
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.progress_uploading));
            mProgressDialog.show();

            mAnimationEntity.setTitle(mImageTitleEditText.getText().toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
