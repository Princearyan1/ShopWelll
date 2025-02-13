package com.princearyan.myutilityapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class UpcomingFeaturesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("Coming Soon");
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);

        setContentView(textView);
    }
}
