package com.example.mybodyfit.activities.settingsActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser fireBaseUser;
    private TextView mail;
    private TextView phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mail = findViewById(R.id.mail_profile);
        phone = findViewById(R.id.phone_profle);
        firebaseAuth = FirebaseAuth.getInstance();
        fireBaseUser = firebaseAuth.getCurrentUser();
        mail.setText(fireBaseUser.getEmail());
        phone.setText(fireBaseUser.getPhoneNumber());
    }
}
