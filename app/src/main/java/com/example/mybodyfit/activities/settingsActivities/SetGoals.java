package com.example.mybodyfit.activities.settingsActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.activities.Home;
import com.example.mybodyfit.constants.AppConstants;
import com.example.mybodyfit.struct.PersonalPreference;
import com.example.mybodyfit.struct.UserName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SetGoals extends AppCompatActivity {

    private EditText caloricGoal;
    private NumberPicker protein;
    private NumberPicker carbs;
    private NumberPicker fats;
    private TextView proteinGrams;
    private TextView carbsGrams;
    private TextView fatsGrams;
    private Button setBtn;
    private PersonalPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        caloricGoal = findViewById(R.id.set_caloric_goal);
        protein = findViewById(R.id.proteinNumPicker);
        carbs = findViewById(R.id.carbNumPicker);
        fats = findViewById(R.id.fatsNumPicker);
        proteinGrams = findViewById(R.id.protein_grams);
        carbsGrams = findViewById(R.id.carbs_grams);
        fatsGrams = findViewById(R.id.fats_grams);
        setBtn = findViewById(R.id.set_goals_btn);
        FirebaseDatabase.getInstance().getReference().child("settings")
                .child(UserName.getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        preference = snapshot.getValue(PersonalPreference.class);
                        setDefaultValues();
                        getCurrentGoals();
                        setGramsByValue();
                        setGramsByCalories();
                        goToSettings();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @SuppressLint("SetTextI18n")
    public void getCurrentGoals() {
        caloricGoal.setText(Integer.toString(preference.caloricGoal));
        protein.setValue((preference.proteinGoal *
                AppConstants.NutrientBreakDown.CALORIES_IN_GRAM_PROTEIN * 100) /
                Integer.parseInt(caloricGoal.getText().toString()));
        carbs.setValue((preference.carbGoal *
                AppConstants.NutrientBreakDown.CALORIES_IN_GRAM_CARB * 100) /
                Integer.parseInt(caloricGoal.getText().toString()));
        fats.setValue((preference.fatGoal *
                AppConstants.NutrientBreakDown.CALORIES_IN_GRAM_FAT * 100) /
                Integer.parseInt(caloricGoal.getText().toString()));
        calcProtein();
        calcCarbs();
        calcFats();
    }

    public void setDefaultValues() {
        protein.setMaxValue(100);
        protein.setMinValue(1);
        protein.setValue(50);
        carbs.setMaxValue(100);
        carbs.setMinValue(1);
        carbs.setValue(30);
        fats.setMaxValue(100);
        fats.setMinValue(1);
        fats.setValue(20);
    }

    public boolean isGoalPossible() {
        return protein.getValue() + carbs.getValue() + fats.getValue() == 100;
    }

    @SuppressLint("SetTextI18n")
    public void setGramsByValue() {
        protein.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (oldVal != newVal) {
                calcProtein();
            } else {
                Toast.makeText(this, "your caloric goal is empty can't calculate the amount of grams", Toast.LENGTH_SHORT).show();
            }
        });

        carbs.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (oldVal != newVal) {
                calcCarbs();

            } else {
                Toast.makeText(this, "your caloric goal is empty can't calculate the amount of grams", Toast.LENGTH_SHORT).show();
            }
        });

        fats.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (oldVal != newVal) {
                calcFats();
            } else {
                Toast.makeText(this, "your caloric goal is empty can't calculate the amount of grams", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setGramsByCalories() {
        caloricGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcProtein();
                calcCarbs();
                calcFats();
            }

            @Override
            public void afterTextChanged(Editable s) {
                calcProtein();
                calcCarbs();
                calcFats();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void calcProtein() {
        if (!caloricGoal.getText().toString().equals("")) {
            int proteinG = (int) (Integer.parseInt(caloricGoal.getText().toString()) * 0.01) * protein.getValue();
            proteinGrams.setText(Integer.toString(proteinG / AppConstants.NutrientBreakDown.CALORIES_IN_GRAM_PROTEIN));
        }
    }

    @SuppressLint("SetTextI18n")
    public void calcCarbs() {
        if (!caloricGoal.getText().toString().equals("")) {
            int carbsG = (int) (Integer.parseInt(caloricGoal.getText().toString()) * 0.01 * carbs.getValue());
            carbsGrams.setText(Integer.toString(carbsG / AppConstants.NutrientBreakDown.CALORIES_IN_GRAM_CARB));
        }
    }

    @SuppressLint("SetTextI18n")
    public void calcFats() {
        if (!caloricGoal.getText().toString().equals("")) {
            int fatsG = (int) (Integer.parseInt(caloricGoal.getText().toString()) * 0.01) * fats.getValue();
            fatsGrams.setText(Integer.toString(fatsG / AppConstants.NutrientBreakDown.CALORIES_IN_GRAM_FAT));
        }
    }

    public void goToSettings() {
        setBtn.setOnClickListener(v -> {
            if (isGoalPossible()) {
                if (!caloricGoal.getText().toString().equals("")) {
                    preference.caloricGoal = Integer.parseInt(caloricGoal.getText().toString());
                    preference.proteinGoal = Integer.parseInt(proteinGrams.getText().toString());
                    preference.carbGoal = Integer.parseInt(carbsGrams.getText().toString());
                    preference.fatGoal = Integer.parseInt(fatsGrams.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("settings")
                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        dataSnapshot.getRef().setValue(preference);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    Toast.makeText(this, "goals are updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                } else {
                    Toast.makeText(this, "you did not set your caloric goal", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "nutrients percentage must be sum to 100", Toast.LENGTH_SHORT).show();
            }
        });
    }
}