package com.example.mybodyfit.activities.settingsActivities;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.struct.NumToTime;
import com.example.mybodyfit.struct.PersonalPreference;

public class SetNotifications extends AppCompatActivity {

    private NumberPicker breakfastHours;
    private NumberPicker breakfastMinutes;
    private NumberPicker lunchHours;
    private NumberPicker lunchMinutes;
    private NumberPicker dinnerHours;
    private NumberPicker dinnerMinutes;
    private Button setBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notfications);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));

        breakfastHours = findViewById(R.id.breakfast_hours);
        breakfastMinutes = findViewById(R.id.breakfast_minutes);
        lunchHours = findViewById(R.id.lunch_hour);
        lunchMinutes = findViewById(R.id.lunch_minutes);
        dinnerHours = findViewById(R.id.dinner_hours);
        dinnerMinutes = findViewById(R.id.dinner_minutes);
        setBtn = findViewById(R.id.set_notifications_btn);

        setClock();
        getTime();
        setTime();
    }

    public void setClock() {
        breakfastHours.setMinValue(0);
        breakfastHours.setMaxValue(59);
        breakfastMinutes.setMinValue(0);
        breakfastMinutes.setMaxValue(59);
        lunchHours.setMinValue(0);
        lunchHours.setMaxValue(59);
        lunchMinutes.setMinValue(0);
        lunchMinutes.setMaxValue(59);
        dinnerHours.setMinValue(0);
        dinnerHours.setMaxValue(59);
        dinnerMinutes.setMinValue(0);
        dinnerMinutes.setMaxValue(59);
    }

    public void getTime() {
        breakfastHours.setValue(NumToTime.getHours(PersonalPreference.breakfastNotification));
        breakfastMinutes.setValue(NumToTime.getHours(PersonalPreference.breakfastNotification));
        lunchHours.setValue(NumToTime.getHours(PersonalPreference.lunchNotification));
        lunchMinutes.setValue(NumToTime.getHours(PersonalPreference.lunchNotification));
        dinnerHours.setValue(NumToTime.getHours(PersonalPreference.dinnerNotification));
        dinnerMinutes.setValue(NumToTime.getHours(PersonalPreference.dinnerNotification));
    }

    public String getBreakfastTime() {
        return NumToTime.getTime(breakfastHours.getValue(), breakfastMinutes.getValue());
    }

    public String getLunchTime() {
        return NumToTime.getTime(lunchHours.getValue(), lunchMinutes.getValue());
    }

    public String getDinnerTime() {
        return NumToTime.getTime(dinnerHours.getValue(), dinnerMinutes.getValue());
    }

    public boolean isAllDifferent() {
        return !getBreakfastTime().equals(getLunchTime()) &&
                !getBreakfastTime().equals(getDinnerTime()) &&
                !getLunchTime().equals(getDinnerTime());
    }

    public void setTime() {
        setBtn.setOnClickListener(v -> {
            if (isAllDifferent()) {
                PersonalPreference.breakfastNotification =
                        NumToTime.getTime(breakfastHours.getValue(), breakfastMinutes.getValue());
                PersonalPreference.lunchNotification =
                        NumToTime.getTime(lunchHours.getValue(), lunchMinutes.getValue());
                PersonalPreference.dinnerNotification =
                        NumToTime.getTime(dinnerHours.getValue(), dinnerMinutes.getValue());
            }
        });
    }
}