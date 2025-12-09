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

public class UpcomingSessionsActivity extends AppCompatActivity {

    private static final String TAG = "UpcomingSessionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_sessions);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnUpcomingTab = findViewById(R.id.btnUpcomingTab);
        Button btnPastTab = findViewById(R.id.btnPastTab);
        TextView upcomingCount = findViewById(R.id.tvUpcomingCount);
        TextView acceptedCount = findViewById(R.id.tvAcceptedCount);
        TextView pendingCount = findViewById(R.id.tvPendingCount);
        RecyclerView recyclerView = findViewById(R.id.rvSessions);

        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navSearch = findViewById(R.id.navSearch);
        LinearLayout navUpcoming = findViewById(R.id.navUpcoming);
        LinearLayout navProfile = findViewById(R.id.navProfile);

        // Back button
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            startActivity(new Intent(this, StudentHomepageActivity.class));
            finish();
        });

        // Tab buttons
        btnUpcomingTab.setOnClickListener(v -> Log.d(TAG, "Upcoming tab clicked (already here)"));
        btnPastTab.setOnClickListener(v -> startActivity(new Intent(this, PastSessionsActivity.class)));

        upcomingCount.setText("3");
        acceptedCount.setText("2");
        pendingCount.setText("1");

        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("Science", "Dr. Sarah", "25 November 2025", "3:00 pm",
                "Room 201, Chapter 5", "Google Meet", SessionStatus.UPCOMING));
        sessions.add(new Session("Physics", "Prof. Bridget", "28 November 2025", "8:00 pm",
                "Fundamentals of laws of gravity", null, SessionStatus.ACCEPTED));
        sessions.add(new Session("Mathematics", "Prof. Michael Chen", "30 November 2025",
                "12:00 pm", "Room 305, Linear Algebra", null, SessionStatus.PENDING, true));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SessionAdapter(sessions,
                session -> Log.d(TAG, "Session clicked: " + session.getSubject()),
                () -> startActivity(new Intent(this, ChatActivity.class))));

        // Bottom navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, StudentHomepageActivity.class)));
        navSearch.setOnClickListener(v -> startActivity(new Intent(this, TutorDiscoveryContainerActivity.class)));
        navUpcoming.setOnClickListener(v -> Log.d(TAG, "Already on Upcoming"));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
