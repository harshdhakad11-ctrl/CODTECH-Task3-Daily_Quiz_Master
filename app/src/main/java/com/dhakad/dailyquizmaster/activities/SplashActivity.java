package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * --------------------------------------------------------
 * SplashActivity
 *
 * Displays splash screen and checks login status.
 *
 * Author : Harsh
 * Project : Daily Quiz Master
 * --------------------------------------------------------
 */

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {

            Intent intent;

            if (mAuth.getCurrentUser() != null) {
                android.util.Log.d("AUTH", "User Logged In : " + mAuth.getCurrentUser().getUid());
            } else {
                android.util.Log.d("AUTH", "User is NULL");
            }

            if (mAuth.getCurrentUser() != null) {

                // User already logged in
                intent = new Intent(
                        SplashActivity.this,
                        HomeActivity.class
                );

            } else {

                // User not logged in
                intent = new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                );

            }

            startActivity(intent);
            finish();

        }, SPLASH_TIME);
    }
}