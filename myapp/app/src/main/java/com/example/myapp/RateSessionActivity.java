package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class RateSessionActivity extends AppCompatActivity {

    private static final String TAG = "RateSessionActivity";

    private ImageButton btnBack;
    private TextView tvTutor, tvSubject, tvDateTime;
    private ImageView star1, star2, star3, star4, star5;
    private List<ImageView> stars;
    private int selectedRating = 0;
    private Button btnSubmit;
    private ChipGroup chipGroup;

    private LinearLayout navHome, navSearch, navUpcoming, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_session);

        btnBack = findViewById(R.id.btnBack);
        tvTutor = findViewById(R.id.tvTutor);
        tvSubject = findViewById(R.id.tvSubject);
        tvDateTime = findViewById(R.id.tvDateTime);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        btnSubmit = findViewById(R.id.btnSubmit);
        chipGroup = findViewById(R.id.chipGroup);

        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        String tutor = getIntent().getStringExtra("tutor");
        String subject = getIntent().getStringExtra("subject");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        tvTutor.setText(tutor != null ? tutor : "Dr. Sarah");
        tvSubject.setText((subject != null ? subject : "Science"));
        tvDateTime.setText((date != null ? date : "25 November 2025") + " • " + (time != null ? time : "3:00 pm"));

        //star rating
        stars = Arrays.asList(star1, star2, star3, star4, star5);
        for (int i = 0; i < stars.size(); i++) {
            final int rating = i + 1;
            stars.get(i).setOnClickListener(v -> {
                selectedRating = rating;
                updateStars(rating);
            });
        }

        // Back button
        btnBack.setOnClickListener(v -> finish());

        //Submit button
        btnSubmit.setOnClickListener(v -> {
            Log.d(TAG, "Submit button clicked, rating: " + selectedRating);
            Toast.makeText(RateSessionActivity.this,
                    "Rating submitted! ⭐ " + selectedRating, Toast.LENGTH_SHORT).show();
            finish();
        });

        //Bottom navigation
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(RateSessionActivity.this, StudentHomepageActivity.class));
            finish();
        });
        navSearch.setOnClickListener(v -> {
            startActivity(new Intent(RateSessionActivity.this, TutorDiscoveryContainerActivity.class));
            finish();
        });
        navUpcoming.setOnClickListener(v -> {
            startActivity(new Intent(RateSessionActivity.this, UpcomingSessionsActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(RateSessionActivity.this, ProfileActivity.class));
            finish();
        });
    }

    private void updateStars(int rating) {
        for (int i = 0; i < stars.size(); i++) {
            if (i < rating) {
                stars.get(i).setImageResource(R.drawable.ic_star_filled);
            } else {
                stars.get(i).setImageResource(R.drawable.ic_star_empty);
            }
        }
    }
}
