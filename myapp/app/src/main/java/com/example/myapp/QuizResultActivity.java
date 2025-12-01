package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    private TextView scoreText, correctCount, wrongCount, totalQuestions;
    private LinearLayout btnNavHome, btnNavSearch, btnNavUpcoming, btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        scoreText = findViewById(R.id.scoreText);
        correctCount = findViewById(R.id.correctCount);
        wrongCount = findViewById(R.id.wrongCount);
        totalQuestions = findViewById(R.id.totalQuestions);

        btnNavHome = findViewById(R.id.navHome);
        btnNavSearch = findViewById(R.id.navSearch);
        btnNavUpcoming = findViewById(R.id.navUpcoming);
        btnNavProfile = findViewById(R.id.navProfile);

        int correct = 8;
        int wrong = 2;
        int total = 10;
        int score = (correct * 100) / total;

        scoreText.setText(score + " %");
        correctCount.setText(String.valueOf(correct));
        wrongCount.setText(String.valueOf(wrong));
        totalQuestions.setText(correct + "/" + total);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        btnNavHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, StudentHomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Navigate to Search and Book
        btnNavSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Upcoming Session
        btnNavUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, UpcomingSessionsFragment.class);
                startActivity(intent);
            }
        });

        // Navigate to Profile
        btnNavProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}