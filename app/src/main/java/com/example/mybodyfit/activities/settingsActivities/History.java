package com.example.mybodyfit.activities.settingsActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mybody.R;
import com.example.mybodyfit.activities.AddFoods;
import com.example.mybodyfit.activities.Home;
import com.example.mybodyfit.activities.Recipes;
import com.example.mybodyfit.activities.Settings;
import com.example.mybodyfit.activities.ViewFoodStats;
import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.recyclerview_adapters.ViewFoodRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ArrayList<Foods> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        bnv = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.history);
        Runnable runnable = () -> {
            foods = new ArrayList<>(MyBodyDatabase.getInstance(this).foodDao().pullAllData());
            setAdapter();
        };
        new Thread(runnable).start();
        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    public void setAdapter() {
        ViewFoodRecyclerViewAdapter adapter = new ViewFoodRecyclerViewAdapter((v, pos) -> {
            goToFood(pos);
        });
        adapter.setFoods(foods);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void goToFood(int pos) {
        Intent intent = new Intent(this, ViewFoodStats.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", foods.get(pos).getFoodName());
        bundle.putString("calories", Double.toString(foods.get(pos).getCalories()));
        bundle.putString("protein", Double.toString(foods.get(pos).getProtein()));
        bundle.putString("carbs", Double.toString(foods.get(pos).getCarbs()));
        bundle.putString("fats", Double.toString(foods.get(pos).getFats()));
        bundle.putString("act","history");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        bnv.setSelectedItemId(R.id.home);
        fab.setOnClickListener(v -> goTo(AddFoods.class));
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    goTo(Home.class);
                    return true;
                case R.id.log:
                    goTo(Log.class);
                    return true;
                case R.id.settings:
                    goTo(Settings.class);
                    return true;
                case R.id.recipes:
                    goTo(Recipes.class);
                    return true;
            }
            return false;
        });
    }

    public void goTo(Class<?> act) {
        startActivity(new Intent(this, act),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}