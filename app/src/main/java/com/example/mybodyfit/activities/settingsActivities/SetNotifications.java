package com.example.mybodyfit.activities.settingsActivities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.activities.Home;
import com.example.mybodyfit.struct.NumToTime;
import com.example.mybodyfit.struct.PersonalPreference;
import com.example.mybodyfit.struct.UserName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetNotifications extends AppCompatActivity {

    private NumberPicker breakfastHours;
    private NumberPicker breakfastMinutes;
    private NumberPicker lunchHours;
    private NumberPicker lunchMinutes;
    private NumberPicker dinnerHours;
    private NumberPicker dinnerMinutes;
    private PersonalPreference preference;
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
        setFormat();
        FirebaseDatabase.getInstance().getReference()
                .child("settings")
                .child(UserName.getName(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                preference = snapshot.getValue(PersonalPreference.class);
                                setClock();
                                getTime();
                                setTime();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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

    @SuppressLint("DefaultLocale")
    public void setFormat() {
        breakfastMinutes.setFormatter(value -> String.format("%02d", value));
        lunchMinutes.setFormatter(value -> String.format("%02d", value));
        dinnerMinutes.setFormatter(value -> String.format("%02d", value));
    }

    public void getTime() {
        breakfastHours.setValue(NumToTime.getHours(preference.breakfastNotification));
        breakfastMinutes.setValue(NumToTime.getMinutes(preference.breakfastNotification));
        lunchHours.setValue(NumToTime.getHours(preference.lunchNotification));
        lunchMinutes.setValue(NumToTime.getMinutes(preference.lunchNotification));
        dinnerHours.setValue(NumToTime.getHours(preference.dinnerNotification));
        dinnerMinutes.setValue(NumToTime.getMinutes(preference.dinnerNotification));
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
                preference.breakfastNotification = NumToTime.getTime(breakfastHours.getValue(), breakfastMinutes.getValue());
                preference.lunchNotification = NumToTime.getTime(lunchHours.getValue(), lunchMinutes.getValue());
                preference.dinnerNotification = NumToTime.getTime(dinnerHours.getValue(), dinnerMinutes.getValue());
                        FirebaseDatabase.getInstance().getReference().child("settings")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dataSnapshot.getRef().setValue(preference);
                                            Toast.makeText(SetNotifications.this, "the notification are updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                startActivity(new Intent(getApplicationContext(), Home.class));
            } else {
                Toast.makeText(this, "all the notifications times needs to be different", Toast.LENGTH_SHORT).show();
            }
        });
    }
}