package com.example.mybodyfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.FireBaseConnection;
import com.example.mybodyfit.struct.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private FireBaseConnection users;
    private User user;
    private EditText nameTxt;
    private EditText usernameTxt;
    private EditText passwordTxt;
    private EditText emailTxt;
    private String userIDTxt;
    private EditText phoneNumberTxt;
    boolean successes = true;
    Button registerBtn;
    FirebaseAuth firebaseAuth;
    char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        firebaseAuth = FirebaseAuth.getInstance();
        users = FireBaseConnection.getInstance();
        nameTxt = findViewById(R.id.name_et);
        usernameTxt = findViewById(R.id.username_et);
        passwordTxt = findViewById(R.id.password_et);
        emailTxt = findViewById(R.id.gmail_et);
        phoneNumberTxt = findViewById(R.id.phone_et);
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(v -> {
            createNewUser();
            startActivity(new Intent(Register.this, LogIn.class));
        });
    }

    public void createNewUser() {
        firebaseAuth.createUserWithEmailAndPassword(emailTxt.getText().toString(),
                passwordTxt.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "you have registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}