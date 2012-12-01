package com.rejasupotaro.dailymotion.ui.helper;

import android.app.Activity;
import android.content.Context;

import com.google.inject.Inject;

public class AbstractActivityHelper {

    @Inject
    private Context mContext;

    protected Activity getActivity() {
        return (Activity) mContext;
    }

    protected Context getContext() {
        return mContext;
    }

    // for test
    public void setContext(Context context) {
        mContext = context;
    }
}
