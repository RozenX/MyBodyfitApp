package com.example.mybodyfit.activities.settingsActivities;


import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;

public class SetNotifications extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notfications);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
    }
}