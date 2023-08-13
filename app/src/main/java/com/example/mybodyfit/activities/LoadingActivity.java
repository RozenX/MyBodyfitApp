package com.example.mybodyfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.UserEatenFoodInADay;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar bar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
//        todo remove it after it works
//        UserEatenFoodInADay.init(this);
//        UserEatenFoodInADay.getInstance().deleteAll();
        bar = findViewById(R.id.loading);
        textView = findViewById(R.id.loading_txt);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        runOnUiThread(() -> new Handler().postDelayed(() -> {
            bar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            if (SystemClock.uptimeMillis() >= 1000) {
                bar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
            startActivity(new Intent(this, Home.class));
            finish();
        }, 1500));

    }
}