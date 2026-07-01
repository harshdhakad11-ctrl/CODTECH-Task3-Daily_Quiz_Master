package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.databinding.ActivityHomeBinding;
import com.dhakad.dailyquizmaster.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!isInternetAvailable()) {

            Toast.makeText(
                    this,
                    "No Internet Connection",
                    Toast.LENGTH_LONG
            ).show();

        }

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Load User Data
        loadUserData();

        // Logout Button
        binding.btnLogout.setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)

                    .setPositiveButton("Yes", (dialog, which) -> {

                        mAuth.signOut();

                        Intent intent = new Intent(
                                HomeActivity.this,
                                LoginActivity.class
                        );

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        finish();

                    })

                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())

                    .show();

        });

        // Start Quiz Button
        binding.btnStartQuiz.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
        });

        // Leaderboard Button
        binding.btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        // Profile Button
        binding.btnProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    ProfileActivity.class
            );

            startActivity(intent);

        });
    }

    /**
     * Load user data from Firestore
     */
    private void loadUserData() {

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        database.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        User user = documentSnapshot.toObject(User.class);

                        if (user != null) {

                            binding.txtUserName.setText(user.getName());
                            binding.txtEmail.setText(user.getEmail());
                            binding.txtCoins.setText(String.valueOf(user.getCoins()));
                            binding.txtScore.setText(String.valueOf(user.getTotalScore()));
                            binding.txtQuizPlayed.setText(String.valueOf(user.getQuizzesPlayed()));

                        }

                    } else {

                        Toast.makeText(
                                HomeActivity.this,
                                "User data not found",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            HomeActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });
    }
    /**
     * Check Internet Connection
     */
    private boolean isInternetAvailable() {

        android.net.ConnectivityManager connectivityManager =
                (android.net.ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            android.net.NetworkInfo networkInfo =
                    connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }
}