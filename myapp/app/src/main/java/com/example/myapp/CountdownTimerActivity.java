package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CountdownTimerActivity extends AppCompatActivity {

    private TextView tvSubject, tvTimer;
    private Button btnStop;
    private LinearLayout navHome, navSearch, navUpcoming, navProfile;
    private CountDownTimer countDownTimer;
    private long totalTimeInMillis;
    private long timeLeftInMillis;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);

        tvSubject = findViewById(R.id.tvSubject);
        tvTimer = findViewById(R.id.tvTimer);
        btnStop = findViewById(R.id.btnStop);

        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        // Get data from intent
        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");
        int hours = intent.getIntExtra("hours", 0);
        int minutes = intent.getIntExtra("minutes", 0);
        int seconds = intent.getIntExtra("seconds", 0);

        tvSubject.setText(subject);

        // Calculate total time in milliseconds
        totalTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L;
        timeLeftInMillis = totalTimeInMillis;

        startTimer();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                long focusedTime = totalTimeInMillis - timeLeftInMillis;

                Intent intent = new Intent(CountdownTimerActivity.this, TimerSessionSummaryActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("focusedTime", focusedTime);
                startActivity(intent);
                finish();
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(CountdownTimerActivity.this, StudentHomepageActivity.class);
                startActivity(intent);
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(CountdownTimerActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        navUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(CountdownTimerActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(CountdownTimerActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(CountdownTimerActivity.this, TimerSessionSummaryActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("focusedTime", totalTimeInMillis);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}