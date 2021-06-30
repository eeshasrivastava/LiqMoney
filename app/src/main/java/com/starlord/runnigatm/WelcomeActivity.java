package com.starlord.runnigatm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    Button signup;
    TextView login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signup = findViewById(R.id.welcome_signup);
        login = findViewById(R.id.welcome_login);
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {
            Intent intent =  new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        login.setOnClickListener(v -> {
            Intent intent =  new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}