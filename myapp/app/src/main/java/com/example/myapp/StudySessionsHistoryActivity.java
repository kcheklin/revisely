package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.StudySession;
import com.example.myapp.models.StudySessionsListResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudySessionsHistoryActivity extends AppCompatActivity {

    private RecyclerView rvStudySessions;
    private ProgressBar progressBar;
    private TextView tvNoSessions;
    private LinearLayout navHome, navSearch, navUpcoming, navProfile;
    private StudySessionAdapter adapter;
    private List<StudySession> studySessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_sessions_history);

        rvStudySessions = findViewById(R.id.rvStudySessions);
        progressBar = findViewById(R.id.progressBar);
        tvNoSessions = findViewById(R.id.tvNoSessions);
        
        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        // Setup RecyclerView
        adapter = new StudySessionAdapter(studySessions);
        rvStudySessions.setLayoutManager(new LinearLayoutManager(this));
        rvStudySessions.setAdapter(adapter);

        // Load study sessions
        loadStudySessions();

        // Navigation
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySessionsHistoryActivity.this, StudentHomepageActivity.class);
                startActivity(intent);
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySessionsHistoryActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        navUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySessionsHistoryActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySessionsHistoryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadStudySessions() {
        progressBar.setVisibility(View.VISIBLE);
        tvNoSessions.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<StudySessionsListResponse> call = apiService.getStudySessions();

        call.enqueue(new Callback<StudySessionsListResponse>() {
            @Override
            public void onResponse(Call<StudySessionsListResponse> call, Response<StudySessionsListResponse> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<StudySession> sessions = response.body().getSessions();
                    if (sessions != null && !sessions.isEmpty()) {
                        studySessions.clear();
                        studySessions.addAll(sessions);
                        adapter.notifyDataSetChanged();
                    } else {
                        tvNoSessions.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(StudySessionsHistoryActivity.this, 
                        "Failed to load sessions", Toast.LENGTH_SHORT).show();
                    tvNoSessions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<StudySessionsListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvNoSessions.setVisibility(View.VISIBLE);
                Toast.makeText(StudySessionsHistoryActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

