package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.dhakad.dailyquizmaster.models.User;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : RegisterActivity
 * Description  : Handles user registration using Firebase Authentication.
 * Author       : Harsh
 * ==========================================================
 */
public class RegisterActivity extends AppCompatActivity {

    // View Binding
    private ActivityRegisterBinding binding;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    // Firestore Database
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Register Button Click
        binding.btnRegister.setOnClickListener(v -> registerUser());

        // Back to Login Screen
        binding.txtLogin.setOnClickListener(v -> finish());
    }

    /**
     * Register new user using Firebase Authentication
     */
    private void registerUser() {

        // Get User Input
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        // Validate Name
        if (TextUtils.isEmpty(name)) {
            binding.etName.setError("Enter Full Name");
            binding.etName.requestFocus();
            return;
        }

        // Validate Email
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Enter Email");
            binding.etEmail.requestFocus();
            return;
        }

        // Validate Password
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Enter Password");
            binding.etPassword.requestFocus();
            return;
        }

        // Password Length Validation
        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        // Confirm Password Validation
        if (!password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Passwords do not match");
            binding.etConfirmPassword.requestFocus();
            return;
        }

        // Show Loading
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);

        // Create User
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        String uid = mAuth.getCurrentUser().getUid();

                        User user = new User(
                                uid,
                                name,
                                email,
                                0,
                                0,
                                0
                        );

                        // Save User Data in Firestore
                        database.collection("users")
                                .document(uid)
                                .set(user)

                                .addOnSuccessListener(unused -> {

                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.btnRegister.setEnabled(true);

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Account Created Successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    startActivity(
                                            new Intent(RegisterActivity.this, HomeActivity.class)
                                    );

                                    finish();

                                })

                                .addOnFailureListener(e -> {

                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.btnRegister.setEnabled(true);

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Firestore Error : " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                });

                    } else {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnRegister.setEnabled(true);

                        Exception exception = task.getException();

                        Toast.makeText(
                                RegisterActivity.this,
                                exception != null
                                        ? exception.getMessage()
                                        : "Registration Failed",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                });
    }
}