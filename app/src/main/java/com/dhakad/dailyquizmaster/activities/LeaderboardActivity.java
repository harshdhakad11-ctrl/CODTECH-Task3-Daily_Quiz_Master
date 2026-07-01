package com.dhakad.dailyquizmaster.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dhakad.dailyquizmaster.adapters.LeaderboardAdapter;
import com.dhakad.dailyquizmaster.databinding.ActivityLeaderboardBinding;
import com.dhakad.dailyquizmaster.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private ActivityLeaderboardBinding binding;

    private FirebaseFirestore database;

    private ArrayList<User> userList;

    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        userList = new ArrayList<>();

        adapter = new LeaderboardAdapter(userList);

        binding.recyclerLeaderboard.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.recyclerLeaderboard.setAdapter(adapter);

        loadLeaderboard();
    }

    private void loadLeaderboard() {

        database.collection("users")
                .orderBy("totalScore", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    userList.clear();

                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        User user = document.toObject(User.class);
                        userList.add(user);

                    }

                    adapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(
                                LeaderboardActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show()

                );

    }
}