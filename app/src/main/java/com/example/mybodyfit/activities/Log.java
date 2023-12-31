package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.UserEatenFoodInADay;
import com.example.mybodyfit.struct.FoodModel;
import com.example.mybodyfit.struct.LogAttributes;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.recyclerview_adapters.LogRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class Log extends AppCompatActivity {

    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Intent intent;
    private UserEatenFoodInADay db;
    private ArrayList<LogAttributes> loggable;
    private TextView caloriesEaten;
    private TextView caloriesLeft;
    private TextView caloricGoal;


    public Log() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        loggable = new ArrayList<>();
        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setSelectedItemId(R.id.log);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.log_recyclerview);
        intent = new Intent(Log.this, ViewFoodEatenByTime.class);
        caloriesEaten = findViewById(R.id.calories_eaten);
        caloriesLeft = findViewById(R.id.calories_left);
        caloricGoal = findViewById(R.id.caloric_goal);
        UserEatenFoodInADay.init(this);
        db = UserEatenFoodInADay.getInstance();

        addLogs();
        setAdapter();
        calculateCurrentCalories();
        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    public void setAdapter() {
        LogRecyclerViewAdapter adapter = new LogRecyclerViewAdapter(loggable, sumCalories(), ((v, position) -> {
            if (v.getId() == R.id.to_view_btn) {
                if (loggable.get(position).getType().equals("Breakfast")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("meal time", "Breakfast");
                    bundle.putInt("meal", FoodModel.BREAKFAST);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            if (v.getId() == R.id.to_view_btn) {
                if (loggable.get(position).getType().equals("Lunch")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("meal time", "Lunch");
                    bundle.putInt("meal", FoodModel.LUNCH);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            if (v.getId() == R.id.to_view_btn) {
                if (loggable.get(position).getType().equals("Dinner")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("meal time", "Dinner");
                    bundle.putInt("meal", FoodModel.DINNER);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            if (v.getId() == R.id.to_view_btn) {
                if (loggable.get(position).getType().equals("Snacks")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("meal time", "Snacks");
                    bundle.putInt("meal", FoodModel.SNACK);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            if (v.getId() == R.id.to_view_btn) {
                if (loggable.get(position).getType().equals("Water")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("meal time", "Water");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void addLogs() {
        loggable.add(new LogAttributes("Breakfast", null));
        loggable.add(new LogAttributes("Lunch", null));
        loggable.add(new LogAttributes("Dinner", null));
        loggable.add(new LogAttributes("Snacks", null));
        loggable.add(new LogAttributes("Water", null));
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        fab.setOnClickListener(v -> goToAddFood());
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    gotToHome();
                    return true;
                case R.id.settings:
                    goToSettings();
                    return true;
                case R.id.recipes:
                    goToRecipes();
                    return true;
            }
            return false;
        });
    }


    public void gotToHome() {
        Intent intent = new Intent(Log.this, Home.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToSettings() {
        Intent intent = new Intent(Log.this, Settings.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToAddFood() {
        Intent intent = new Intent(Log.this, AddFoods.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToRecipes() {
        Intent intent = new Intent(Log.this, Recipes.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    @SuppressLint("SetTextI18n")
    public void calculateCurrentCalories() {
        caloriesEaten.setText(Integer.toString(caloriesEaten()));
        int caloriesLeft = Integer.parseInt(caloricGoal.getText().toString()) - caloriesEaten();
        this.caloriesLeft.setText(Integer.toString(caloriesLeft));
    }

    @SuppressLint("SetTextI18n")
    public int caloriesEaten() {
        return Arrays.stream(sumCalories()).sum();
    }

    public int[] sumCalories() {
        int[] calories = new int[4];
        ArrayList<FoodModel> breakfast = new ArrayList<>(db.pullFoods(FoodModel.BREAKFAST));
        ArrayList<FoodModel> lunch = new ArrayList<>(db.pullFoods(FoodModel.LUNCH));
        ArrayList<FoodModel> dinner = new ArrayList<>(db.pullFoods(FoodModel.DINNER));
        ArrayList<FoodModel> snacks = new ArrayList<>(db.pullFoods(FoodModel.SNACK));

        for (int i = 0; i < breakfast.size(); i++) {
            calories[0] += breakfast.get(i).getCalories();
        }
        for (int i = 0; i < lunch.size(); i++) {
            calories[1] += lunch.get(i).getCalories();
        }
        for (int i = 0; i < dinner.size(); i++) {
            calories[2] += dinner.get(i).getCalories();
        }
        for (int i = 0; i < snacks.size(); i++) {
            calories[3] += snacks.get(i).getCalories();
        }
        return calories;
    }
}