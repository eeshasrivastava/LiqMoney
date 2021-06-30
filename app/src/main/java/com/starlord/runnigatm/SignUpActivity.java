package com.starlord.runnigatm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private EditText firstName, lastName, email, password, pin;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email_singUp);
        password = findViewById(R.id.password_signUp);
        pin = findViewById(R.id.pin);
        Button signUpBtn = findViewById(R.id.signUp);
        TextView cancelText = findViewById(R.id.signup_cancel);

        // Checking if the user is already signed in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        signUpBtn.setOnClickListener(view -> {
            final String firstNameValue = firstName.getText().toString().trim();
            final String lastNameValue = lastName.getText().toString().trim();
            String emailValue = email.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();
            String pinValue = pin.getText().toString().trim();

            if (TextUtils.isEmpty(firstNameValue)) {
                firstName.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(lastNameValue)) {
                lastName.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(emailValue)) {
                email.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(passwordValue)) {
                password.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(pinValue)) {
                pin.setError("Required field");
                return;
            }
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            sendEmailVerification();

                            //Sending user details to Fire Store collection named users
                            final FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            Map<String, Object> userDetails = new HashMap<>();
                            userDetails.put("firstName", firstNameValue);
                            userDetails.put("lastName", lastNameValue);
                            userDetails.put("email", emailValue);
                            userDetails.put("pin", pinValue);

                            DocumentReference documentReference = db.collection("users").document(userID);
                            documentReference.set(userDetails).addOnSuccessListener(aVoid -> Log.d(TAG, "User profile updated."))
                                    .addOnFailureListener(e -> Log.d(TAG, "User profile not updated."));

                            //update DisplayName of the user
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstNameValue).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "Display name updated.");
                                        }
                                    });

                            //After user registration
                            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign Up failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        });

        cancelText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    // Email sent
                    Log.d(TAG, "Verification email sent");
                    Toast.makeText(getApplicationContext(),
                            "Sign Up Successful and verification link is send to the registered email.",
                            Toast.LENGTH_LONG).show();
                });
    }
}