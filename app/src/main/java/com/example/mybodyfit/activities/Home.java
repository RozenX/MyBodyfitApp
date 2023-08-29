package com.example.mybodyfit.activities;


import static com.example.mybodyfit.constants.AppConstants.Weight.deficitToFatLoss;
import static com.example.mybodyfit.constants.AppConstants.Weight.surplusToFatGain;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.struct.CurrentDate;
import com.example.mybodyfit.struct.FoodModel;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.PersonalPreference;
import com.example.mybodyfit.struct.UserName;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Home extends AppCompatActivity {

    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private NestedScrollView scrollView;
    private PersonalPreference preference;
    private ProgressBar caloriesBar;
    private ProgressBar proteinBar;
    private ProgressBar carbsBar;
    private ProgressBar fatsBar;
    private ProgressBar accuracy;
    int calories;
    int protein;
    int carbs;
    int fats;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        scrollView = findViewById(R.id.scrollView);
        bnv = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        caloriesBar = findViewById(R.id.pb_calories);

        proteinBar = findViewById(R.id.pb_protein);
        proteinBar.getProgressDrawable().setColorFilter((Color.parseColor("#D69E10")), android.graphics.PorterDuff.Mode.SRC_IN);

        carbsBar = findViewById(R.id.pb_carbs);
        carbsBar.getProgressDrawable().setColorFilter((Color.parseColor("#D69E10")), android.graphics.PorterDuff.Mode.SRC_IN);

        fatsBar = findViewById(R.id.pb_fats);
        fatsBar.getProgressDrawable().setColorFilter((Color.parseColor("#D69E10")), android.graphics.PorterDuff.Mode.SRC_IN);
        accuracy = findViewById(R.id.accuracy);
        TextView accuracyTxt = findViewById(R.id.accuracyText);
        getFoodEatenToday();
//        PersonalPreference personalPreference = new PersonalPreference();
//        personalPreference.setDefaultSettings();
//
//        FirebaseDatabase.getInstance().getReference().child("settings").child(UserName.getName(FirebaseAuth.getInstance().getCurrentUser().getEmail())).setValue(personalPreference);
        FirebaseDatabase.getInstance().getReference().child("settings")
                .child(UserName.getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        preference = snapshot.getValue(PersonalPreference.class);
                        runOnUiThread(() -> new Handler().post(() -> {
                            caloriesBar.setProgress((int) (100 * ((double) calories / preference.caloricGoal)));
                            proteinBar.setProgress((int) (100 * ((double) protein / preference.proteinGoal)));
                            carbsBar.setProgress((int) (100 * ((double) carbs / preference.carbGoal)));
                            fatsBar.setProgress((int) (100 * ((double) fats / preference.fatGoal)));
                            calculateAccuracy();
                            accuracyTxt.setText(accuracy.getProgress() + "%");

                            if (caloriesBar.getProgress() == 0 && calories != 0) {
                                recreate();
                            }
                        }));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        graphJourney();
        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    public void graphJourney() {
        LineChart lineChart = (LineChart) findViewById(R.id.weightChart);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(
                new String[]{
                        "week 1",
                        "week 2",
                        "week 3",
                        "week 4",
                        "week 5",
                        "week 6",
                        "week 7",
                        "week 8"}));
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setTextColor(Color.WHITE);
        lineChart.getDescription().setText("weight journey");
        lineChart.getLegend().setTextColor(Color.WHITE);
        ArrayList<Entry> weights = new ArrayList<>();
        float weight = 70;
        for (int i = 0; i < 8; i++) {
            weights.add(new Entry(i, weight));
            weight = (float) calcWeeklyWeight(weight, 1500, 1000);
        }
        LineDataSet lineDataSet = new LineDataSet(weights, "weight journey");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.parseColor("#D69E10"));
        lineChart.setData(new LineData(lineDataSet));
        lineChart.setVisibleXRangeMaximum(10f);
        lineChart.setBackgroundColor(Color.parseColor("#181818"));
        lineChart.setBorderColor(Color.WHITE);
        lineChart.setNoDataTextColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);
        manageNavigation();
    }


    public double calcWeeklyWeight(double weight, double maintenance, double caloricIntake) {
        double deficit = caloricIntake - maintenance;
        if (deficit < 0) {
            return (double) ((weight - deficitToFatLoss(maintenance, caloricIntake)));
        } else if (deficit > 0) {
            return (double) (weight + surplusToFatGain(maintenance, caloricIntake));
        }
        return (double) weight;
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        bnv.setSelectedItemId(R.id.home);
        fab.setOnClickListener(v -> goToAddFood());
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.log:
                    goToLog();
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

    public void goToLog() {
        Intent intent = new Intent(Home.this, Log.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToSettings() {
        Intent intent = new Intent(Home.this, Settings.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToAddFood() {
        Intent intent = new Intent(Home.this, AddFoods.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToRecipes() {
        Intent intent = new Intent(Home.this, Recipes.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void getFoodEatenToday() {
        Runnable runnable = () -> {
            ArrayList<Foods> breakfast = new ArrayList<>(MyBodyDatabase.getInstance(this).
                    foodDao().pullByMealAndDateData(FoodModel.BREAKFAST, CurrentDate.getDateWithoutTimeUsingCalendar()));
            ArrayList<Foods> lunch = new ArrayList<>(MyBodyDatabase.getInstance(this).
                    foodDao().pullByMealAndDateData(FoodModel.LUNCH, CurrentDate.getDateWithoutTimeUsingCalendar()));
            ArrayList<Foods> dinner = new ArrayList<>(MyBodyDatabase.getInstance(this).
                    foodDao().pullByMealAndDateData(FoodModel.DINNER, CurrentDate.getDateWithoutTimeUsingCalendar()));
            ArrayList<Foods> snacks = new ArrayList<>(MyBodyDatabase.getInstance(this).
                    foodDao().pullByMealAndDateData(FoodModel.SNACK, CurrentDate.getDateWithoutTimeUsingCalendar()));
            ArrayList<Foods> foods = new ArrayList<>();
            foods.addAll(breakfast);
            foods.addAll(lunch);
            foods.addAll(dinner);
            foods.addAll(snacks);
            if (foods.size() > 0) {
                for (Foods model : foods) {
                    calories += model.getCalories();
                    protein += model.getProtein();
                    carbs += model.getCarbs();
                    fats += model.getFats();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void calculateAccuracy() {
        int caloriesPer = caloriesBar.getProgress();
        int proteinPer = proteinBar.getProgress();
        int carbsPer = carbsBar.getProgress();
        int fatsPer = fatsBar.getProgress();

        accuracy.setProgress((caloriesPer + proteinPer + carbsPer + fatsPer) / 4);
    }
}