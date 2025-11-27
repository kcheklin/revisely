package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimerSessionSummaryActivity extends AppCompatActivity {

    private TextView tvSubjectName, tvFocusTime;
    private Button btnStartNewSession, btnSaveFinish;
    private LinearLayout navHome, navSearch, navUpcoming, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_session_summary);

        tvSubjectName = findViewById(R.id.tvSubjectName);
        tvFocusTime = findViewById(R.id.tvFocusTime);
        btnStartNewSession = findViewById(R.id.btnStartNewSession);
        btnSaveFinish = findViewById(R.id.btnSaveFinish);

        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        // Get data from intent
        Intent intent = getIntent();
        String subject = intent.getStringExtra("subject");
        long focusedTime = intent.getLongExtra("focusedTime", 0);

        tvSubjectName.setText(subject);

        // Format the focused time
        int hours = (int) (focusedTime / 1000) / 3600;
        int minutes = (int) ((focusedTime / 1000) % 3600) / 60;
        int seconds = (int) (focusedTime / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        tvFocusTime.setText(timeFormatted);

        btnStartNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, CountdownTimerInitialActivity.class);
                startActivity(intent);
            }
        });

        btnSaveFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, StudentHomepageActivity.class);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, StudentHomepageActivity.class);
                startActivity(intent);
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, TutorDiscoveryFragment.class);
                startActivity(intent);
            }
        });

        navUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, UpcomingSessionsFragment.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
