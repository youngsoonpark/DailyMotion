package com.rejasupotaro.dailymotion.ui;

import com.rejasupotaro.dailymotion.R;
import com.rejasupotaro.dailymotion.R.id;
import com.rejasupotaro.dailymotion.R.layout;
import com.rejasupotaro.dailymotion.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button closeButton = (Button) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                launchTimelineActivity(MainActivity.this, TimelineActivity.class);
            }
        });
    }
    
    private void launchTimelineActivity(Context context, Class targetClass) {
        context.startActivity(new Intent(context, targetClass));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
