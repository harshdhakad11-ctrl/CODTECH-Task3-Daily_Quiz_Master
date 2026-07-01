package com.dhakad.dailyquizmaster.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.databinding.ActivityUploadQuestionsBinding;
import com.dhakad.dailyquizmaster.models.Question;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.dhakad.dailyquizmaster.models.Category;

public class UploadQuestionsActivity extends AppCompatActivity {

    private ActivityUploadQuestionsBinding binding;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityUploadQuestionsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        binding.btnUpload.setOnClickListener(v -> {

            uploadCategory("computer");
            uploadCategory("gk");
            uploadCategory("science");
            uploadCategory("mathematics");
            uploadCategory("geography");
            uploadCategory("history");
            uploadCategory("sports");
            uploadCategory("current_affairs");

        });

    }

    private ArrayList<Question> loadQuestionsFromJson(String fileName) {

        ArrayList<Question> questionList = new ArrayList<>();

        try {

            InputStream inputStream = getAssets().open(fileName + ".json");

            int size = inputStream.available();

            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);

            Type type = new TypeToken<ArrayList<Question>>() {}.getType();

            questionList = new Gson().fromJson(json, type);

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Error : " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        }

        return questionList;
    }

    private void uploadCategory(String categoryId) {

        ArrayList<Question> questionList = loadQuestionsFromJson(categoryId);

        if (questionList.isEmpty()) {

            Toast.makeText(
                    this,
                    categoryId + " : No Questions Found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Category Name
        String name;

        switch (categoryId) {

            case "computer":
                name = "Computer";
                break;

            case "gk":
                name = "General Knowledge";
                break;

            case "science":
                name = "Science";
                break;

            case "mathematics":
                name = "Mathematics";
                break;

            case "geography":
                name = "Geography";
                break;

            case "history":
                name = "History";
                break;

            case "sports":
                name = "Sports";
                break;

            case "current_affairs":
                name = "Current Affairs";
                break;

            default:
                name = categoryId;
                break;
        }

        final String categoryName = name;

        // Create Category Document
        Category category = new Category(
                categoryId,
                categoryName,
                questionList.size()
        );

        database.collection("categories")
                .document(categoryId)
                .set(category)
                .addOnSuccessListener(unused -> {

                    // Upload Questions
                    for (Question question : questionList) {

                        database.collection("categories")
                                .document(categoryId)
                                .collection("questions")
                                .add(question);

                    }

                    Toast.makeText(
                            UploadQuestionsActivity.this,
                            categoryName + " Uploaded Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(
                                UploadQuestionsActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()

                );
    }
}