package com.dhakad.dailyquizmaster.models;

public class User {

    private String uid;
    private String name;
    private String email;
    private int coins;
    private int totalScore;
    private int quizzesPlayed;

    // Required for Firebase
    public User() {
    }

    // Constructor
    public User(String uid, String name, String email, int coins, int totalScore, int quizzesPlayed) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.coins = coins;
        this.totalScore = totalScore;
        this.quizzesPlayed = quizzesPlayed;
    }

    // Getters

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getCoins() {
        return coins;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getQuizzesPlayed() {
        return quizzesPlayed;
    }

    // Setters

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setQuizzesPlayed(int quizzesPlayed) {
        this.quizzesPlayed = quizzesPlayed;
    }
}