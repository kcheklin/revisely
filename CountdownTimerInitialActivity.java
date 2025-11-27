package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import androidx.appcompat.app.AppCompatActivity;

public class CountdownTimerInitialActivity extends AppCompatActivity {

    private EditText etSubject;
    private NumberPicker hourPicker, minutePicker, secondPicker;
    private Button btnStart;
    private LinearLayout navHome, navSearch, navUpcoming, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer_initial);

        etSubject = findViewById(R.id.etSubject);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);
        btnStart = findViewById(R.id.btnStart);

        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        // Setup NumberPickers
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(0);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(0);

        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setValue(0);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = etSubject.getText().toString();
                int hours = hourPicker.getValue();
                int minutes = minutePicker.getValue();
                int seconds = secondPicker.getValue();

                Intent intent = new Intent(CountdownTimerInitialActivity.this, CountdownTimerActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("hours", hours);
                intent.putExtra("minutes", minutes);
                intent.putExtra("seconds", seconds);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountdownTimerInitialActivity.this, StudentHomepageActivity.class);
                startActivity(intent);
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountdownTimerInitialActivity.this, TutorDiscoveryFragment.class);
                startActivity(intent);
            }
        });

        navUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountdownTimerInitialActivity.this, UpcomingSessionsFragment.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountdownTimerInitialActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
