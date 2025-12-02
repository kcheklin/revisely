package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class StudentHomepageActivity extends AppCompatActivity {

    private Button btnCountdownTimer, btnTutorDiscovery, btnMySessions, btnQuizzes;
    private LinearLayout navHome, navSearch, navUpcoming, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);

        btnCountdownTimer = findViewById(R.id.btnCountdownTimer);
        btnTutorDiscovery = findViewById(R.id.btnTutorDiscovery);
        btnMySessions = findViewById(R.id.btnMySessions);
        btnQuizzes = findViewById(R.id.btnQuizzes);

        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        btnCountdownTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, CountdownTimerInitialActivity.class);
                startActivity(intent);
            }
        });

        btnTutorDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        btnMySessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
            }
        });

        btnQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, QuizSelectionActivity.class);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on homepage
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        navUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomepageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}