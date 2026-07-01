package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.R;
import com.dhakad.dailyquizmaster.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtEmail, txtCoins, txtScore, txtQuizPlayed;
    private Button btnBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtCoins = findViewById(R.id.txtCoins);
        txtScore = findViewById(R.id.txtScore);
        txtQuizPlayed = findViewById(R.id.txtQuizPlayed);
        btnBack = findViewById(R.id.btnBack);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        loadProfile();

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * Load User Profile
     */
    private void loadProfile() {

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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

                            txtName.setText(user.getName());
                            txtEmail.setText(user.getEmail());
                            txtCoins.setText("Coins : " + user.getCoins());
                            txtScore.setText("Total Score : " + user.getTotalScore());
                            txtQuizPlayed.setText("Quizzes Played : " + user.getQuizzesPlayed());

                        }

                    } else {

                        Toast.makeText(
                                ProfileActivity.this,
                                "User data not found",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                })
                .addOnFailureListener(e ->

                        Toast.makeText(
                                ProfileActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()

                );
    }
}