package com.rejasupotaro.dailymotion.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class AnimationView extends ImageView {

    private static final String TAG = AnimationView.class.getSimpleName();
    private static final int DEFAULT_ANIMATION_INTERVAL = 200;

    private List<Bitmap> mBitmapList;
    private int animationInterval = DEFAULT_ANIMATION_INTERVAL;
    private Thread mThread;
    private Handler mHandler;
    private int showingIndex;

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmapList = new ArrayList<Bitmap>();
        mHandler = new Handler();
        showingIndex = 0;
    }

    public void setupAnimation(List<Bitmap> bitmapList) {
        mBitmapList = bitmapList;

        mThread = new Thread(new Runnable() {
            public void run() {
                while(true){
                    try {
                        Thread.sleep(animationInterval);
                    }catch(InterruptedException e){
                        Log.v(TAG, "Something wrong with AnimationView", e);
                    }
                    mHandler.post(new Runnable() {
                        public void run() {
                            if (showingIndex >= mBitmapList.size()) {
                                showingIndex = 0;
                            }
                            setImageBitmap(mBitmapList.get(showingIndex++));
                        }
                    });
                }
            }
        });

        mThread.start();
    }

    public void setAnimationInterval(int per) {
        animationInterval = (int) (DEFAULT_ANIMATION_INTERVAL * (per / 100.0)) + 30;
    }
}
