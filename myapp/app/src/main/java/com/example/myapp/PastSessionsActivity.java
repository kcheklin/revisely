package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PastSessionsActivity extends AppCompatActivity {

    private static final String TAG = "PastSessionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_sessions);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnUpcomingTab = findViewById(R.id.btnUpcomingTab);
        Button btnPastTab = findViewById(R.id.btnPastTab);
        TextView pastCount = findViewById(R.id.tvPastCount);
        RecyclerView recyclerView = findViewById(R.id.rvSessions);

        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navSearch = findViewById(R.id.navSearch);
        LinearLayout navUpcoming = findViewById(R.id.navUpcoming);
        LinearLayout navProfile = findViewById(R.id.navProfile);

        // Back button
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "Back clicked");
            startActivity(new Intent(this, UpcomingSessionsActivity.class));
            finish();
        });

        // Tab buttons
        btnUpcomingTab.setOnClickListener(v -> startActivity(new Intent(this, UpcomingSessionsActivity.class)));
        btnPastTab.setOnClickListener(v -> Log.d(TAG, "Past tab clicked (already here)"));

        List<Session> pastSessions = new ArrayList<>();
        pastSessions.add(new Session("Science", "Dr. Sarah", "25 November 2025", "3:00 pm",
                "Room 201, Chapter 5", "Google Meet", SessionStatus.COMPLETED));
        pastSessions.add(new Session("Science", "Dr. Sarah", "7 November 2025", "7:30 pm",
                "Room 105, Organic Chemistry", "Microsoft Teams", SessionStatus.COMPLETED));

        pastCount.setText(String.valueOf(pastSessions.size()));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PastSessionAdapter(pastSessions, session ->
                startActivity(new Intent(this, RateSessionActivity.class))));

        // Bottom navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, StudentHomepageActivity.class)));
        navSearch.setOnClickListener(v -> startActivity(new Intent(this, TutorDiscoveryContainerActivity.class)));
        navUpcoming.setOnClickListener(v -> startActivity(new Intent(this, UpcomingSessionsActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
