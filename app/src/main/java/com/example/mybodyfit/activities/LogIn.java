package com.example.mybodyfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.FireBaseConnection;

public class LogIn extends AppCompatActivity {

    private TextView changePassword;
    private TextView signIn;
    private FireBaseConnection users;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        users = FireBaseConnection.getInstance();
        username = findViewById(R.id.username_Login);
        password = findViewById(R.id.password_Login);
        signIn = findViewById(R.id.sign_in);
        signIn.setTextColor(Color.parseColor("#0f6fe3"));
        signIn.animate().setDuration(500).alpha(1);
        signIn.setOnClickListener(v -> startActivity(new Intent(LogIn.this, Register.class)));
        changePassword = findViewById(R.id.change_password);
        changePassword.setTextColor(Color.parseColor("#0f6fe3"));

        changePassword.animate().setDuration(500).alpha(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}