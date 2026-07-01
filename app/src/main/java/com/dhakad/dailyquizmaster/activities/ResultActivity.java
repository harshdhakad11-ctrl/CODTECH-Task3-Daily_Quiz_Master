package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : ResultActivity
 * Description  : Displays quiz result and updates user data.
 * Author       : Harsh
 * ==========================================================
 */

public class ResultActivity extends AppCompatActivity {

    // ==========================================================
    // Views
    // ==========================================================

    private TextView txtScore;
    private Button btnPlayAgain;
    private Button btnHome;

    // ==========================================================
    // Firebase
    // ==========================================================

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    // ==========================================================
    // Quiz Data
    // ==========================================================

    private int score;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Initialize Views
        txtScore = findViewById(R.id.txtScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnHome = findViewById(R.id.btnHome);

        // Receive Quiz Result
        score = getIntent().getIntExtra("score", 0);
        totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        // Display Score
        txtScore.setText(score + " / " + totalQuestions);

        // Update User Data
        updateUserData();

        // Play Again
        btnPlayAgain.setOnClickListener(v -> {

            Intent intent = new Intent(ResultActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();

        });

        // Back To Home
        btnHome.setOnClickListener(v -> {

            Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        });

    }

    /**
     * ==========================================================
     * Update User Coins, Score and Quiz Count
     * ==========================================================
     */
    private void updateUserData() {

        if (mAuth.getCurrentUser() == null) {

            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        database.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (!documentSnapshot.exists()) {

                        Toast.makeText(
                                ResultActivity.this,
                                "User document not found!",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    // Read old values
                    Long oldCoins = documentSnapshot.getLong("coins");
                    Long oldScore = documentSnapshot.getLong("totalScore");
                    Long oldPlayed = documentSnapshot.getLong("quizzesPlayed");

                    int coins = oldCoins == null ? 0 : oldCoins.intValue();
                    int totalScore = oldScore == null ? 0 : oldScore.intValue();
                    int quizzesPlayed = oldPlayed == null ? 0 : oldPlayed.intValue();

                    // Update values
                    coins += (score * 10);
                    totalScore += score;
                    quizzesPlayed++;

                    // Save updated values
                    database.collection("users")
                            .document(uid)
                            .update(
                                    "coins", coins,
                                    "totalScore", totalScore,
                                    "quizzesPlayed", quizzesPlayed
                            )
                            .addOnSuccessListener(unused ->

                                    Toast.makeText(
                                            ResultActivity.this,
                                            "User data updated successfully",
                                            Toast.LENGTH_SHORT
                                    ).show()

                            )
                            .addOnFailureListener(e ->

                                    Toast.makeText(
                                            ResultActivity.this,
                                            "Update Failed : " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show()

                            );

                })
                .addOnFailureListener(e ->

                        Toast.makeText(
                                ResultActivity.this,
                                "Read Failed : " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()

                );
    }
}