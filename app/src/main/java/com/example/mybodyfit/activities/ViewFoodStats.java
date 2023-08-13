package com.example.mybodyfit.activities;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.UserEatenFoodInADay;
import com.example.mybodyfit.struct.FoodModel;
import com.example.mybodyfit.struct.MenuThread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ViewFoodStats extends AppCompatActivity {

    private int mealTime;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private UserEatenFoodInADay db;
    private TextView foodName;
    private TextView calories;
    private TextView protein;
    private TextView carbs;
    private TextView fats;
    private EditText amount;
    private Button showBtn;
    private Button addBtn;
    private Button pop;
    private String mealTimeTxt;
    double calories_;
    double protein_;
    double carbs_;
    double fats_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_stats);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        bnv = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        foodName = findViewById(R.id.food_view_name);
        calories = findViewById(R.id.food_calories_view);
        protein = findViewById(R.id.food_protein_view);
        carbs = findViewById(R.id.food_carbs_view);
        fats = findViewById(R.id.food_fats_view);
        amount = findViewById(R.id.grams);
        showBtn = findViewById(R.id.show_btn);
        addBtn = findViewById(R.id.add_food_btn);
        pop = findViewById(R.id.meal_time_btn);
        UserEatenFoodInADay.init(ViewFoodStats.this);
        db = UserEatenFoodInADay.getInstance();
        foodName.setText(getIntent().getStringExtra("name"));
        pop.setOnClickListener(this::showPopUp);
        setStats();
        setStatsBasedOnGrams();
        addFoodsToUser();
        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    @SuppressLint("SetTextI18n")
    public void setStats() {
        // gets the data from the api.
        if (amount.getText() == null || amount.getText().toString().equals("")) {
            calories_ = Double.parseDouble(getIntent().getStringExtra("calories"));
            protein_ = Double.parseDouble(getIntent().getStringExtra("protein"));
            carbs_ = Double.parseDouble(getIntent().getStringExtra("carbs"));
            fats_ = Double.parseDouble(getIntent().getStringExtra("fats"));
        } else {
            calories_ *= Integer.parseInt(amount.getText().toString());
            protein_ *= Integer.parseInt(amount.getText().toString());
            carbs_ *= Integer.parseInt(amount.getText().toString());
            fats_ *= Integer.parseInt(amount.getText().toString());
        }
        //  sets the text to the usable data.
        this.calories.setText(Double.toString(calories_));
        this.protein.setText(Double.toString(protein_));
        this.carbs.setText(Double.toString(carbs_));
        this.fats.setText(Double.toString(fats_));

        calories_ = Double.parseDouble(getIntent().getStringExtra("calories"));
        protein_ = Double.parseDouble(getIntent().getStringExtra("protein"));
        carbs_ = Double.parseDouble(getIntent().getStringExtra("carbs"));
        fats_ = Double.parseDouble(getIntent().getStringExtra("fats"));
    }

    public void setStatsBasedOnGrams() {
        showBtn.setOnClickListener(v -> setStats());
    }

    public void addFoodsToUser() {
        addBtn.setOnClickListener(v -> {
            if (mealTime == 0) {
                Toast.makeText(this, "meal time field is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ViewFoodStats.this, ViewFoodEatenByTime.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", foodName.getText().toString());
                bundle.putString("mealTime", Integer.toString(mealTime));
                bundle.putString("mealTimeInText", mealTimeTxt);
                bundle.putString("calories_", calories.getText().toString());
                bundle.putString("protein_", protein.getText().toString());
                bundle.putString("carbs_", carbs.getText().toString());
                bundle.putString("fats_", fats.getText().toString());
                bundle.putString("amount", amount.getText().toString());
                bundle.putString("from", "ViewFoodStats");
                intent.putExtras(bundle);
                addFoodsToUserHistory();
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        fab.setOnClickListener(v -> goToAddFood());
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
        Intent intent = new Intent(ViewFoodStats.this, Home.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToLog() {
        Intent intent = new Intent(ViewFoodStats.this, Log.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }


    public void goToSettings() {
        Intent intent = new Intent(ViewFoodStats.this, Settings.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToAddFood() {
        Intent intent = new Intent(ViewFoodStats.this, AddFoods.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToRecipes() {
        Intent intent = new Intent(ViewFoodStats.this, Recipes.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void addFoodsToUserHistory() {
        String amount;
        if (!this.amount.getText().toString().equals("")) {
            amount = this.amount.getText().toString();
        } else {
            amount = "1";
        }
        boolean isSuccessful;
        if (mealTime == 0) {
            Toast.makeText(this, "meal time field is empty", Toast.LENGTH_SHORT).show();
        } else {
                isSuccessful = db.addFood(new FoodModel(foodName.getText().toString(),
                        Double.parseDouble(calories.getText().toString()),
                        Double.parseDouble(protein.getText().toString()),
                        Double.parseDouble(carbs.getText().toString()),
                        Double.parseDouble(fats.getText().toString()),
                        mealTime).setAmount(Double.parseDouble(amount)));
            if (isSuccessful) {
                Toast.makeText(this, "food was added :)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "something is fucked", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showPopUp(View v) {
        PopupMenu popUp = new PopupMenu(ViewFoodStats.this, v);
        popUp.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.breakfast) {
                mealTime = FoodModel.BREAKFAST;
                mealTimeTxt = "Breakfast";
                Toast.makeText(this, "meal is set to breakfast", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.lunch) {
                mealTime = FoodModel.LUNCH;
                mealTimeTxt = "Lunch";
                Toast.makeText(this, "meal is set to lunch", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.dinner) {
                mealTime = FoodModel.DINNER;
                mealTimeTxt = "Dinner";
                Toast.makeText(this, "meal is set to dinner", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.snack) {
                mealTime = FoodModel.SNACK;
                mealTimeTxt = "Snack";
                Toast.makeText(this, "meal is set to snack", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popUp.inflate(R.menu.pop_up_meal_time);
        popUp.show();
    }
}