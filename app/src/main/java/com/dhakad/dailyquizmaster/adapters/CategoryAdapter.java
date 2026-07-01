package com.dhakad.dailyquizmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.dailyquizmaster.activities.QuizActivity;
import com.dhakad.dailyquizmaster.databinding.ItemCategoryBinding;
import com.dhakad.dailyquizmaster.models.Category;

import java.util.ArrayList;
import com.dhakad.dailyquizmaster.R;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : CategoryAdapter
 * Description  : Adapter for displaying quiz categories.
 * Author       : Harsh
 * ==========================================================
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // Context
    private final Context context;

    // Category List
    private final ArrayList<Category> categoryList;

    /**
     * Constructor
     */
    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category category = categoryList.get(position);

        holder.binding.txtCategoryName.setText(category.getCategoryName());

        holder.binding.txtTotalQuestions.setText(
                category.getTotalQuestions() + " Questions"
        );

        // Set Category Icon
        switch (category.getCategoryName()) {

            case "Computer":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_computer);
                break;

            case "Science":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_science);
                break;

            case "Mathematics":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_math);
                break;

            case "History":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_history);
                break;

            case "Geography":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_geography);
                break;

            case "General Knowledge":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_quizplayed);
                break;

            case "Current Affairs":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_news);
                break;

            case "Sports":
                holder.binding.imgCategory.setImageResource(R.drawable.ic_sports);
                break;

            default:
                holder.binding.imgCategory.setImageResource(R.drawable.ic_category);
                break;
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, QuizActivity.class);

            intent.putExtra("categoryId", category.getCategoryId());
            intent.putExtra("categoryName", category.getCategoryName());

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    /**
     * ViewHolder Class
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}