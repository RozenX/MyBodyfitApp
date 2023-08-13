package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.activities.settingsActivities.SetGoals;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.SettingsAttributes;
import com.example.mybodyfit.struct.recyclerview_adapters.SettingsRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ArrayList<SettingsAttributes> settable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));

        settable = new ArrayList<>();
        fab = findViewById(R.id.fab);
        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setSelectedItemId(R.id.settings);
        recyclerView = findViewById(R.id.recycler_view);

        addSettings();
        setAdapter();

        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        fab.setOnClickListener(v -> goToAddFood());
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    gotToHome();
                    return true;
                case R.id.log:
                    goToLog();
                    return true;
                case R.id.recipes:
                    goToRecipes();
                    return true;
            }
            return false;
        });
    }

    public void setAdapter() {
        SettingsRecyclerViewAdapter adapter = new SettingsRecyclerViewAdapter(settable, ((v, position) -> {
            if (settable.get(position).getType().equals("Log Out")) {
                Intent intent = new Intent(Settings.this, LogIn.class);
                startActivity(intent);
            }
            if (settable.get(position).getType().equals("Set Goals")) {
                Intent intent = new Intent(Settings.this, SetGoals.class);
                startActivity(intent);
            }
        }));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void addSettings() {
        settable.add(new SettingsAttributes("Notifications", null));
        settable.add(new SettingsAttributes("Set Goals", R.drawable.baseline_flag_24));
        settable.add(new SettingsAttributes("Profile", R.drawable.baseline_person_pin_24));
        settable.add(new SettingsAttributes("Track steps", null));
        settable.add(new SettingsAttributes("Set Notifications", null));
        settable.add(new SettingsAttributes("Log Out", R.drawable.baseline_logout_24));
    }

    public void gotToHome() {
        Intent intent = new Intent(Settings.this, Home.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToLog() {
        Intent intent = new Intent(Settings.this, Log.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToAddFood() {
        Intent intent = new Intent(Settings.this, AddFoods.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToRecipes() {
        Intent intent = new Intent(Settings.this, Recipes.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}