package com.dhakad.dailyquizmaster.models;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : Category
 * Description  : Model class for Quiz Categories.
 * Author       : Harsh
 * ==========================================================
 */

public class Category {

    // ==========================================================
    // Category Document ID
    // Example : gk, science, maths
    // ==========================================================

    private String categoryId;

    // ==========================================================
    // Category Name
    // Example : General Knowledge
    // ==========================================================

    private String categoryName;

    // ==========================================================
    // Total Questions
    // ==========================================================

    private int totalQuestions;

    // ==========================================================
    // Required Empty Constructor
    // ==========================================================

    public Category() {
    }

    // ==========================================================
    // Parameterized Constructor
    // ==========================================================

    public Category(String categoryId,
                    String categoryName,
                    int totalQuestions) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalQuestions = totalQuestions;
    }

    // ==========================================================
    // Getter & Setter
    // ==========================================================

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}