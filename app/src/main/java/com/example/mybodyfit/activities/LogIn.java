package com.example.mybodyfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.firebase.FireBaseConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    private TextView changePassword;
    private TextView signIn;
    private FireBaseConnection users;
    private EditText gmail;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        users = FireBaseConnection.getInstance();
        gmail = findViewById(R.id.gmail_login);
        password = findViewById(R.id.password_Login);
        signIn = findViewById(R.id.sign_in);
        signIn.setTextColor(Color.parseColor("#0f6fe3"));
        signIn.animate().setDuration(500).alpha(1);
        signIn.setOnClickListener(v -> startActivity(new Intent(LogIn.this, Register.class)));
        changePassword = findViewById(R.id.change_password);
        changePassword.setTextColor(Color.parseColor("#0f6fe3"));

        changePassword.animate().setDuration(500).alpha(1);
        login = findViewById(R.id.button);
        login.setOnClickListener(this::login);
    }

    public void login(View v) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(gmail.getText().toString(),
                password.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FireBaseConnection.init(this);
                FireBaseConnection.getInstance().addFoodsByUser(MyBodyDatabase.getInstance(this).foodDao());
                startActivity(new Intent(this, Home.class));
            } else {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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