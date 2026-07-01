package com.dhakad.dailyquizmaster.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.dailyquizmaster.databinding.ItemLeaderboardBinding;
import com.dhakad.dailyquizmaster.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final ArrayList<User> userList;

    public LeaderboardAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLeaderboardBinding binding = ItemLeaderboardBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);

        // Medal / Rank
        if (position == 0) {
            holder.binding.txtRank.setText("🥇");
        } else if (position == 1) {
            holder.binding.txtRank.setText("🥈");
        } else if (position == 2) {
            holder.binding.txtRank.setText("🥉");
        } else {
            holder.binding.txtRank.setText(String.valueOf(position + 1));
        }

        holder.binding.txtName.setText(user.getName());
        holder.binding.txtScore.setText(String.valueOf(user.getTotalScore()));

        // Default
        holder.binding.txtYou.setVisibility(android.view.View.GONE);
        holder.binding.cardLeaderboard.setCardBackgroundColor(Color.WHITE);

        holder.binding.txtName.setTextColor(Color.BLACK);
        holder.binding.txtScore.setTextColor(Color.BLACK);

        // Top 3 Gold
        if (position < 3) {

            holder.binding.txtName.setTextColor(Color.parseColor("#FFD700"));
            holder.binding.txtScore.setTextColor(Color.parseColor("#FFD700"));

        }

        // Highlight Logged In User
        String currentUid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "";

        if (user.getUid() != null && user.getUid().equals(currentUid)) {

            holder.binding.txtYou.setVisibility(android.view.View.VISIBLE);

            holder.binding.cardLeaderboard.setCardBackgroundColor(
                    Color.parseColor("#E3F2FD")
            );

        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemLeaderboardBinding binding;

        public ViewHolder(ItemLeaderboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}