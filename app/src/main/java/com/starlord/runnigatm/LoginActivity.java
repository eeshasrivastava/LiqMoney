package com.starlord.runnigatm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button signInBtn;
    private TextView resetText;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private TextView cancelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_singIn);
        password = findViewById(R.id.password_signIn);
        signInBtn = findViewById(R.id.signIn);
        resetText = findViewById(R.id.reset_tv);
        cancelText = findViewById(R.id.signIn_cancel);

        signInBtn.setOnClickListener(view -> {
            String emailValue = email.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();

            if (TextUtils.isEmpty(emailValue)) {
                email.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(passwordValue)) {
                password.setError("Required field");
                return;
            }
            progressDialog.setMessage("Processing...");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailValue, passwordValue).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            if (mAuth.getCurrentUser().isEmailVerified()) {
//                                Toast.makeText(LoginActivity.this,
//                                "You signed in successfully",
//                                 Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "User email is not verified, please verify the email and SigIn again.",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Login failed, please check your internet connection",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

        });

        resetText.setOnClickListener(view -> resetDialog());

        cancelText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void resetDialog() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.password_reset_layout, null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(view);
        dialog.setCancelable(false);

        final EditText email = view.findViewById(R.id.email_reset);
        Button cancel = view.findViewById(R.id.cancel_reset);
        Button send = view.findViewById(R.id.send_reset);

        send.setOnClickListener(view1 -> {
            String emailValue = email.getText().toString().trim();
            if (TextUtils.isEmpty(emailValue)) {
                email.setError("Required");
                return;
            }
            mAuth.sendPasswordResetEmail(emailValue).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Password reset link has been sent to your registered email id",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            dialog.dismiss();
        });

        cancel.setOnClickListener(view12 -> dialog.dismiss());

        dialog.show();
    }
}