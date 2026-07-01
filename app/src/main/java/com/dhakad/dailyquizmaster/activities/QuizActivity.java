package com.dhakad.dailyquizmaster.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhakad.dailyquizmaster.databinding.ActivityQuizBinding;
import com.dhakad.dailyquizmaster.models.Question;

import java.util.ArrayList;
import android.os.Handler;
import com.dhakad.dailyquizmaster.R;
import android.media.MediaPlayer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * ==========================================================
 * Project      : Daily Quiz Master
 * Class Name   : QuizActivity
 * Description  : Displays quiz questions and handles quiz logic.
 * Author       : Harsh
 * ==========================================================
 */

public class QuizActivity extends AppCompatActivity {

    // View Binding
    private ActivityQuizBinding binding;

    // Firestore


    // Question List
    private ArrayList<Question> questionList;

    // Quiz Variables
    private int index = 0;
    private int score = 0;

    private String categoryId;
    private String categoryName;

    // Prevent Multiple Clicks
    private boolean answered = false;

    // Timer
    private CountDownTimer countDownTimer;
    private static final long TIMER_TIME = 30000;

    // Sound Effects
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);



        questionList = new ArrayList<>();

        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");

        loadQuestions();

        binding.btnNext.setOnClickListener(v -> checkAnswer());
    }

    /**
     * Load Questions
     */
    private void loadQuestions() {

        String fileName = categoryId + ".json";

        try {

            InputStream inputStream = getAssets().open(fileName);

            int size = inputStream.available();

            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);

            Type type = new TypeToken<ArrayList<Question>>() {}.getType();

            questionList = new Gson().fromJson(json, type);

            // Random Questions
            Collections.shuffle(questionList);

            // Sirf 10 Questions
            if (questionList.size() > 10) {

                questionList = new ArrayList<>(
                        questionList.subList(0,10)
                );

            }

            showQuestion();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        }

    }

    /**
     * Show Question
     */
    private void showQuestion() {

        Question question = questionList.get(index);

        binding.txtQuestionNo.setText(
                "Question " + (index + 1) + " / " + questionList.size()
        );

        int progress = ((index + 1) * 100) / questionList.size();
        binding.progressBar.setProgress(progress);

        binding.txtQuestion.setText(question.getQuestion());

        binding.rbOption1.setText(question.getOption1());
        binding.rbOption2.setText(question.getOption2());
        binding.rbOption3.setText(question.getOption3());
        binding.rbOption4.setText(question.getOption4());

        binding.radioGroup.clearCheck();

        binding.txtTimer.setTextColor(
                getResources().getColor(android.R.color.holo_orange_light)

        );

        startTimer();

        answered = false;

        binding.rbOption1.setEnabled(true);
        binding.rbOption2.setEnabled(true);
        binding.rbOption3.setEnabled(true);
        binding.rbOption4.setEnabled(true);

        binding.rbOption1.setTextColor(Color.WHITE);
        binding.rbOption2.setTextColor(Color.WHITE);
        binding.rbOption3.setTextColor(Color.WHITE);
        binding.rbOption4.setTextColor(Color.WHITE);
    }
    /**
     * ==========================================================
     * Check Selected Answer
     * ==========================================================
     */
    private void checkAnswer() {

        if (answered)
            return;

        answered = true;

        if (countDownTimer != null)
            countDownTimer.cancel();

        if (binding.radioGroup.getCheckedRadioButtonId() == -1) {

            Toast.makeText(
                    this,
                    "Please select an answer",
                    Toast.LENGTH_SHORT
            ).show();

            answered = false;
            startTimer();
            return;
        }

        Question question = questionList.get(index);

        String selectedAnswer = "";

        if (binding.rbOption1.isChecked())
            selectedAnswer = binding.rbOption1.getText().toString();

        else if (binding.rbOption2.isChecked())
            selectedAnswer = binding.rbOption2.getText().toString();

        else if (binding.rbOption3.isChecked())
            selectedAnswer = binding.rbOption3.getText().toString();

        else if (binding.rbOption4.isChecked())
            selectedAnswer = binding.rbOption4.getText().toString();

        // Disable Options
        binding.rbOption1.setEnabled(false);
        binding.rbOption2.setEnabled(false);
        binding.rbOption3.setEnabled(false);
        binding.rbOption4.setEnabled(false);

        // Highlight Correct Answer
        if (question.getAnswer().equals(binding.rbOption1.getText().toString()))
            binding.rbOption1.setTextColor(getColor(R.color.correct_green));

        if (question.getAnswer().equals(binding.rbOption2.getText().toString()))
            binding.rbOption2.setTextColor(getColor(R.color.correct_green));

        if (question.getAnswer().equals(binding.rbOption3.getText().toString()))
            binding.rbOption3.setTextColor(getColor(R.color.correct_green));

        if (question.getAnswer().equals(binding.rbOption4.getText().toString()))
            binding.rbOption4.setTextColor(getColor(R.color.correct_green));

        // Wrong Answer
        if (!selectedAnswer.equals(question.getAnswer())) {

            if (wrongSound != null)
                wrongSound.start();

            if(binding.rbOption1.isChecked())
                binding.rbOption1.setTextColor(getColor(R.color.wrong_red));

            if(binding.rbOption2.isChecked())
                binding.rbOption2.setTextColor(getColor(R.color.wrong_red));

            if(binding.rbOption3.isChecked())
                binding.rbOption3.setTextColor(getColor(R.color.wrong_red));

            if(binding.rbOption4.isChecked())
                binding.rbOption4.setTextColor(getColor(R.color.wrong_red));

        } else {

            score++;

            if (correctSound != null)
                correctSound.start();
        }

        // Wait 1.5 seconds then move to next question
        new Handler().postDelayed(() -> {

            index++;

            if (index < questionList.size()) {

                showQuestion();

            } else {

                Intent intent = new Intent(
                        QuizActivity.this,
                        ResultActivity.class
                );

                intent.putExtra("score", score);
                intent.putExtra("totalQuestions", questionList.size());

                startActivity(intent);
                finish();

            }

        }, 1500);

    }

    /**
     * ==========================================================
     * Start Timer
     * ==========================================================
     */
    private void startTimer() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(TIMER_TIME, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                binding.txtTimer.setText((millisUntilFinished / 1000) + " Sec");

                if (millisUntilFinished <= 10000) {
                    binding.txtTimer.setTextColor(Color.RED);
                }

            }

            @Override
            public void onFinish() {

                answered = true;

                index++;

                if (index < questionList.size()) {

                    showQuestion();

                } else {

                    Intent intent = new Intent(
                            QuizActivity.this,
                            ResultActivity.class
                    );

                    intent.putExtra("score", score);
                    intent.putExtra("totalQuestions", questionList.size());

                    startActivity(intent);
                    finish();

                }

            }

        }.start();

    }

    /**
     * ==========================================================
     * Cancel Timer
     * ==========================================================
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (correctSound != null) {
            correctSound.release();
        }

        if (wrongSound != null) {
            wrongSound.release();
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}