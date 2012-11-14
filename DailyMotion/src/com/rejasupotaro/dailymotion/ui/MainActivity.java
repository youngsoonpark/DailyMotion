package com.rejasupotaro.dailymotion.ui;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.R.id;
import com.rejasupotaro.dailymotion.R.layout;
import com.rejasupotaro.dailymotion.R.menu;
import com.rejasupotaro.dailymotion.ui.helper.DailyMotionHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView animationView = (ImageView) findViewById(R.id.image_animation_view);
        animationView.setOnClickListener(new View.OnClickListener() {
            
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
