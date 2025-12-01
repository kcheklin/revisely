package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class QuizSelectionActivity extends AppCompatActivity {

    private Spinner subjectSpinner, gradeSpinner, chapterSpinner;
    private Button startQuizButton;
    private LinearLayout btnNavHome, btnNavSearch, btnNavUpcoming, btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        gradeSpinner = findViewById(R.id.gradeSpinner);
        chapterSpinner = findViewById(R.id.chapterSpinner);
        startQuizButton = findViewById(R.id.startQuizButton);

        btnNavHome = findViewById(R.id.navHome);
        btnNavSearch = findViewById(R.id.navSearch);
        btnNavUpcoming = findViewById(R.id.navUpcoming);
        btnNavProfile = findViewById(R.id.navProfile);

        setupSpinners();

        setupBottomNavigation();

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSpinners() {
        String[] subjects = {"Select a subject", "Mathematics", "Physics", "Chemistry", "Biology"};
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        String[] grades = {"Select your grade", "Grade 10", "Grade 11", "Grade 12"};
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, grades);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);

        String[] chapters = {"Select a chapter", "Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4"};
        ArrayAdapter<String> chapterAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chapters);
        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(chapterAdapter);
    }

    private void setupBottomNavigation() {
        btnNavHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, StudentHomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Navigate to Search and Book
        btnNavSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this,  TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Upcoming Session
        btnNavUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, UpcomingSessionsFragment.class);
                startActivity(intent);
            }
        });

        // Navigate to Profile
        btnNavProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}