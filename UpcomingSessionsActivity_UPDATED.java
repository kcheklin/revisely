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

public class UpcomingSessionsActivity extends AppCompatActivity {

    private static final String TAG = "UpcomingSessionsActivity";

    private RecyclerView recyclerView;
    private TextView upcomingCount, acceptedCount, pendingCount;
    private ProgressBar progressBar;
    private TextView tvNoSessions;
    private SessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_sessions);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnUpcomingTab = findViewById(R.id.btnUpcomingTab);
        Button btnPastTab = findViewById(R.id.btnPastTab);
        upcomingCount = findViewById(R.id.tvUpcomingCount);
        acceptedCount = findViewById(R.id.tvAcceptedCount);
        pendingCount = findViewById(R.id.tvPendingCount);
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
            Log.d(TAG, "Back button clicked");
            startActivity(new Intent(this, StudentHomepageActivity.class));
            finish();
        });

        // Tab buttons
        btnUpcomingTab.setOnClickListener(v -> Log.d(TAG, "Upcoming tab clicked (already here)"));
        btnPastTab.setOnClickListener(v -> startActivity(new Intent(this, PastSessionsActivity.class)));

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Load sessions from backend
        loadUpcomingSessions();

        // Bottom navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, StudentHomepageActivity.class)));
        navSearch.setOnClickListener(v -> startActivity(new Intent(this, TutorDiscoveryContainerActivity.class)));
        navUpcoming.setOnClickListener(v -> Log.d(TAG, "Already on Upcoming"));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void loadUpcomingSessions() {
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
            tvNoSessions.setText("Please login to view sessions");
            return;
        }

        // Make API call
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<List<Booking>> call = apiService.getUpcomingSessions(userId);

        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    
                    if (bookings.isEmpty()) {
                        tvNoSessions.setVisibility(View.VISIBLE);
                        tvNoSessions.setText("No upcoming sessions");
                        recyclerView.setVisibility(View.GONE);
                        updateCounts(0, 0, 0);
                    } else {
                        // Convert bookings to sessions
                        List<Session> sessions = convertBookingsToSessions(bookings);
                        
                        // Update UI
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new SessionAdapter(sessions,
                                session -> Log.d(TAG, "Session clicked: " + session.getSubject()),
                                () -> startActivity(new Intent(UpcomingSessionsActivity.this, ChatActivity.class)));
                        recyclerView.setAdapter(adapter);
                        
                        // Update counts
                        updateCounts(sessions);
                    }
                } else {
                    Log.e(TAG, "API Error: " + response.code() + " - " + response.message());
                    Toast.makeText(UpcomingSessionsActivity.this, 
                            "Failed to load sessions: " + response.message(), 
                            Toast.LENGTH_SHORT).show();
                    tvNoSessions.setVisibility(View.VISIBLE);
                    tvNoSessions.setText("Failed to load sessions");
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvNoSessions.setVisibility(View.VISIBLE);
                tvNoSessions.setText("Network error. Please try again.");
                
                Log.e(TAG, "Network Error: " + t.getMessage(), t);
                Toast.makeText(UpcomingSessionsActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Session> convertBookingsToSessions(List<Booking> bookings) {
        List<Session> sessions = new ArrayList<>();
        
        for (Booking booking : bookings) {
            // Determine session status
            SessionStatus status;
            switch (booking.getStatus()) {
                case "Upcoming":
                    status = SessionStatus.UPCOMING;
                    break;
                case "Accepted":
                    status = SessionStatus.ACCEPTED;
                    break;
                case "Pending":
                    status = SessionStatus.PENDING;
                    break;
                default:
                    status = SessionStatus.UPCOMING;
            }

            // Create session object
            // Note: You may need to add tutor name to Booking model or fetch separately
            Session session = new Session(
                    booking.getSubject(),           // subject
                    "Tutor",                        // tutorName (enhance this later)
                    booking.getDate(),              // date
                    booking.getTime(),              // time
                    "Session details",              // location/details
                    "Meeting link",                 // meetingLink (add to Booking if needed)
                    status                          // status
            );
            
            sessions.add(session);
        }
        
        return sessions;
    }

    private void updateCounts(List<Session> sessions) {
        int upcoming = 0, accepted = 0, pending = 0;
        
        for (Session session : sessions) {
            switch (session.getStatus()) {
                case UPCOMING:
                    upcoming++;
                    break;
                case ACCEPTED:
                    accepted++;
                    break;
                case PENDING:
                    pending++;
                    break;
            }
        }
        
        updateCounts(upcoming + accepted + pending, accepted, pending);
    }

    private void updateCounts(int total, int accepted, int pending) {
        upcomingCount.setText(String.valueOf(total));
        acceptedCount.setText(String.valueOf(accepted));
        pendingCount.setText(String.valueOf(pending));
    }
}

