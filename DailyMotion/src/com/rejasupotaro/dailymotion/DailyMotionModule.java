package com.rejasupotaro.dailymotion;

import roboguice.inject.ContextSingleton;

import com.google.inject.AbstractModule;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionActivityHelper;

public class DailyMotionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DailyMotionActivityHelper.class).in(ContextSingleton.class);
    }

}
