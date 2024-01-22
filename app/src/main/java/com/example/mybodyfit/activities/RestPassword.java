package com.example.mybodyfit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mybody.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class RestPassword extends AppCompatActivity {

    private EditText email;
    private Button push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);

        email = findViewById(R.id.email);
        push = findViewById(R.id.rest_btn);

        push.setOnClickListener(v -> FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "password rest has been sent", Toast.LENGTH_SHORT).show();
            }
        }));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LogIn.class));
    }
}