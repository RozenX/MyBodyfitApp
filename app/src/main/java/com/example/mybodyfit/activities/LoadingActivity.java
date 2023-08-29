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
import com.example.mybodyfit.dataBase.firebase.FireBaseConnection;
import com.example.mybodyfit.struct.NutrientsEatenToday;
import com.google.firebase.auth.FirebaseAuth;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar bar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent;
        FireBaseConnection.init(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            NutrientsEatenToday.rest();
            intent = new Intent(this, Home.class);
        } else {
            intent = new Intent(this, LogIn.class);
        }
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
            startActivity(intent);
            finish();
        }, 1500));
    }
}