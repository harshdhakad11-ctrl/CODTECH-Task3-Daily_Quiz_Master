package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : LoginActivity
 * Description  : Handles user login using Firebase Authentication.
 * Author       : Harsh
 * ==========================================================
 */

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Login
        binding.btnLogin.setOnClickListener(v -> loginUser());

        // Register
        binding.txtSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        // Forgot Password
        binding.txtForgotPassword.setOnClickListener(v -> resetPassword());
    }

    /**
     * Login User
     */
    private void loginUser() {

        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Enter Email");
            binding.etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Enter Password");
            binding.etPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    // Check user after login
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        android.util.Log.d(
                                "LOGIN_TEST",
                                "UID = " + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        );
                    } else {
                        android.util.Log.d("LOGIN_TEST", "User is NULL");
                    }

                    Toast.makeText(
                            LoginActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(
                                LoginActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()

                );
    }

    /**
     * Forgot Password
     */
    private void resetPassword() {

        String email = binding.etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            binding.etEmail.setError("Enter your email first");
            binding.etEmail.requestFocus();
            return;

        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused ->

                        Toast.makeText(
                                LoginActivity.this,
                                "Password reset email sent.",
                                Toast.LENGTH_LONG
                        ).show()

                )
                .addOnFailureListener(e ->

                        Toast.makeText(
                                LoginActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()

                );
    }
}