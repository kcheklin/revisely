package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.CreateStudySessionRequest;
import com.example.myapp.models.StudySessionResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerSessionSummaryActivity extends AppCompatActivity {

    private TextView tvSubjectName, tvFocusTime;
    private Button btnStartNewSession, btnSaveFinish;
    private LinearLayout navHome, navSearch, navUpcoming, navProfile;
    private String subject;
    private long focusedTime;
    private long startTime;

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
        subject = intent.getStringExtra("subject");
        focusedTime = intent.getLongExtra("focusedTime", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

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
                saveStudySession();
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
                Intent intent = new Intent(TimerSessionSummaryActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        navUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSessionSummaryActivity.this, UpcomingSessionsActivity.class);
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

    private void saveStudySession() {
        try {
            // Convert focused time from milliseconds to minutes
            int durationInMinutes = (int) (focusedTime / 1000 / 60);
            
            // If duration is less than 1 minute, set it to 1
            if (durationInMinutes < 1) {
                durationInMinutes = 1;
            }
            
            // Format start time to ISO 8601 format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            String startTimeFormatted = sdf.format(new Date(startTime));
            
            // Create request
            CreateStudySessionRequest request = new CreateStudySessionRequest(
                subject,
                durationInMinutes,
                startTimeFormatted
            );
            
            // Call API
            ApiService apiService = RetrofitClient.getAuthenticatedApiService(TimerSessionSummaryActivity.this);
            Call<StudySessionResponse> call = apiService.createStudySession(request);
            
            call.enqueue(new Callback<StudySessionResponse>() {
                @Override
                public void onResponse(Call<StudySessionResponse> call, Response<StudySessionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(TimerSessionSummaryActivity.this, 
                            "Study session saved successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TimerSessionSummaryActivity.this, StudentHomepageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TimerSessionSummaryActivity.this, 
                            "Failed to save session. Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        // Still navigate to homepage even if save fails
                        Intent intent = new Intent(TimerSessionSummaryActivity.this, StudentHomepageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                
                @Override
                public void onFailure(Call<StudySessionResponse> call, Throwable t) {
                    Toast.makeText(TimerSessionSummaryActivity.this, 
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    // Still navigate to homepage even if save fails
                    Intent intent = new Intent(TimerSessionSummaryActivity.this, StudentHomepageActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "Error saving session: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TimerSessionSummaryActivity.this, StudentHomepageActivity.class);
            startActivity(intent);
            finish();
        }
    }
}