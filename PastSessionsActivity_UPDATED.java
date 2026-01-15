package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.Booking;
import com.example.myapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastSessionsActivity extends AppCompatActivity {

    private static final String TAG = "PastSessionsActivity";

    private RecyclerView recyclerView;
    private TextView pastCount;
    private ProgressBar progressBar;
    private TextView tvNoSessions;
    private PastSessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_sessions);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnUpcomingTab = findViewById(R.id.btnUpcomingTab);
        Button btnPastTab = findViewById(R.id.btnPastTab);
        pastCount = findViewById(R.id.tvPastCount);
        recyclerView = findViewById(R.id.rvSessions);
        
        // Add these to your layout XML if they don't exist
        progressBar = findViewById(R.id.progressBar);
        tvNoSessions = findViewById(R.id.tvNoSessions);

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

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load past sessions from backend
        loadPastSessions();

        // Bottom navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, StudentHomepageActivity.class)));
        navSearch.setOnClickListener(v -> startActivity(new Intent(this, TutorDiscoveryContainerActivity.class)));
        navUpcoming.setOnClickListener(v -> startActivity(new Intent(this, UpcomingSessionsActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void loadPastSessions() {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvNoSessions.setVisibility(View.GONE);

        // Get userId from SessionManager
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        if (userId == 0) {
            Log.e(TAG, "User ID not found. User may not be logged in.");
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            tvNoSessions.setVisibility(View.VISIBLE);
            tvNoSessions.setText("Please login to view past sessions");
            return;
        }

        // Make API call
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<List<Booking>> call = apiService.getPastSessions(userId);

        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();

                    if (bookings.isEmpty()) {
                        tvNoSessions.setVisibility(View.VISIBLE);
                        tvNoSessions.setText("No past sessions");
                        recyclerView.setVisibility(View.GONE);
                        pastCount.setText("0");
                    } else {
                        // Convert bookings to sessions
                        List<Session> sessions = convertBookingsToSessions(bookings);

                        // Update UI
                        recyclerView.setVisibility(View.VISIBLE);
                        pastCount.setText(String.valueOf(sessions.size()));
                        
                        adapter = new PastSessionAdapter(sessions, session -> {
                            // Pass booking info to RateSessionActivity
                            Intent intent = new Intent(PastSessionsActivity.this, RateSessionActivity.class);
                            intent.putExtra("tutor", session.getTutorName());
                            intent.putExtra("subject", session.getSubject());
                            intent.putExtra("date", session.getDate());
                            intent.putExtra("time", session.getTime());
                            // Add booking ID and tutor ID for review submission
                            // intent.putExtra("bookingId", booking.getId());
                            // intent.putExtra("tutorId", booking.getTutorId());
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Log.e(TAG, "API Error: " + response.code() + " - " + response.message());
                    Toast.makeText(PastSessionsActivity.this,
                            "Failed to load sessions: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                    tvNoSessions.setVisibility(View.VISIBLE);
                    tvNoSessions.setText("Failed to load past sessions");
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvNoSessions.setVisibility(View.VISIBLE);
                tvNoSessions.setText("Network error. Please try again.");

                Log.e(TAG, "Network Error: " + t.getMessage(), t);
                Toast.makeText(PastSessionsActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Session> convertBookingsToSessions(List<Booking> bookings) {
        List<Session> sessions = new ArrayList<>();

        for (Booking booking : bookings) {
            // Past sessions are typically "Completed" or "Cancelled"
            SessionStatus status = SessionStatus.COMPLETED;

            // Create session object
            Session session = new Session(
                    booking.getSubject(),           // subject
                    "Tutor",                        // tutorName (enhance with actual tutor name)
                    booking.getDate(),              // date
                    booking.getTime(),              // time
                    "Session details",              // location/details
                    "Meeting link",                 // meetingLink
                    status                          // status
            );

            sessions.add(session);
        }

        return sessions;
    }
}

