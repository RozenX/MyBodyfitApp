package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.dao.FoodDao;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.struct.CurrentDate;
import com.example.mybodyfit.struct.FoodModel;
import com.example.mybodyfit.struct.MenuThread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class AddFoods extends AppCompatActivity {

    private Button addButton;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private EditText foodName, calories, protein, carbs, fats;
    private FoodDao database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_foods);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        addButton = findViewById(R.id.add_btn);
        database = MyBodyDatabase.getInstance(this).foodDao();
        try {
            addButton.setOnClickListener(v -> addNewFood());
        } catch (Exception e) {
        }

        bnv = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        foodName = findViewById(R.id.food_name);
        calories = findViewById(R.id.food_calories);
        protein = findViewById(R.id.food_protein);
        carbs = findViewById(R.id.food_carbs);
        fats = findViewById(R.id.food_fats);

        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    public void addNewFood() {
        try {
            Runnable runnable = () -> {
                database.insert(new Foods(foodName.getText().toString() + "-quick add",
                        Integer.parseInt(calories.getText().toString()),
                        Integer.parseInt(protein.getText().toString()),
                        Integer.parseInt(carbs.getText().toString()),
                        Integer.parseInt(fats.getText().toString()),
                        FoodModel.SNACK,
                        999, CurrentDate.getDateWithoutTimeUsingCalendar()));
            };
            new Thread(runnable).start();
            Toast.makeText(this, "food was added", Toast.LENGTH_SHORT).show();
            goToHome();
        } catch (Exception e) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    goToHome();
                    return true;
                case R.id.settings:
                    goToSettings();
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

    public void goToHome() {
        Intent intent = new Intent(AddFoods.this, Home.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToSettings() {
        Intent intent = new Intent(AddFoods.this, Settings.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToLog() {
        Intent intent = new Intent(AddFoods.this, Log.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToRecipes() {
        Intent intent = new Intent(AddFoods.this, Recipes.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}