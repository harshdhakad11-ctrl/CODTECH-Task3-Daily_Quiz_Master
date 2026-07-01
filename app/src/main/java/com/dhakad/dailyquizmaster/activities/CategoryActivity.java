package com.dhakad.dailyquizmaster.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dhakad.dailyquizmaster.adapters.CategoryAdapter;
import com.dhakad.dailyquizmaster.databinding.ActivityCategoryBinding;
import com.dhakad.dailyquizmaster.models.Category;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : CategoryActivity
 * Description  : Displays all quiz categories.
 * Author       : Harsh
 * ==========================================================
 */

public class CategoryActivity extends AppCompatActivity {

    // View Binding
    private ActivityCategoryBinding binding;

    // Firestore
    private FirebaseFirestore database;

    // Category List
    private ArrayList<Category> categoryList;

    // RecyclerView Adapter
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        database = FirebaseFirestore.getInstance();

        // Initialize List
        categoryList = new ArrayList<>();

        // Setup RecyclerView
        binding.recyclerCategory.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new CategoryAdapter(this, categoryList);

        binding.recyclerCategory.setAdapter(adapter);

        // Load Categories
        loadCategories();
    }

    /**
     * ==========================================================
     * Load Categories from Firestore
     * ==========================================================
     */
    private void loadCategories() {

        database.collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    categoryList.clear();

                    for (var document : queryDocumentSnapshots) {

                        Category category = document.toObject(Category.class);

                        categoryList.add(category);
                    }

                    adapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(
                                CategoryActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }
}