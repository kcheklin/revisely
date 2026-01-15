package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.ReviewRequest;
import com.example.myapp.models.ReviewResponse;
import com.example.myapp.utils.SessionManager;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateSessionActivity extends AppCompatActivity {

    private static final String TAG = "RateSessionActivity";

    private ImageButton btnBack;
    private TextView tvTutor, tvSubject, tvDateTime;
    private ImageView star1, star2, star3, star4, star5;
    private EditText etReviewContent;
    private List<ImageView> stars;
    private int selectedRating = 0;
    private Button btnSubmit;
    private ChipGroup chipGroup;

    private LinearLayout navHome, navSearch, navUpcoming, navProfile;

    // Session details
    private int bookingId;
    private int tutorId;
    private String tutorName;
    private String subject;

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
        etReviewContent = findViewById(R.id.etReviewContent); // Add this EditText to your layout
        btnSubmit = findViewById(R.id.btnSubmit);
        chipGroup = findViewById(R.id.chipGroup);

        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navUpcoming = findViewById(R.id.navUpcoming);
        navProfile = findViewById(R.id.navProfile);

        // Get session details from intent
        bookingId = getIntent().getIntExtra("bookingId", 0);
        tutorId = getIntent().getIntExtra("tutorId", 0);
        tutorName = getIntent().getStringExtra("tutor");
        subject = getIntent().getStringExtra("subject");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        // Display session details
        tvTutor.setText(tutorName != null ? tutorName : "Dr. Sarah");
        tvSubject.setText(subject != null ? subject : "Science");
        tvDateTime.setText((date != null ? date : "25 November 2025") + " • " + (time != null ? time : "3:00 pm"));

        // Star rating setup
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

        // Submit button
        btnSubmit.setOnClickListener(v -> submitReview());

        // Bottom navigation
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

    private void submitReview() {
        Log.d(TAG, "Submit button clicked, rating: " + selectedRating);

        // Validate rating
        if (selectedRating == 0) {
            Toast.makeText(this, "Please select a star rating", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate review content
        String reviewContent = etReviewContent.getText().toString().trim();
        if (reviewContent.isEmpty()) {
            Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get user info
        SessionManager sessionManager = new SessionManager(this);
        String studentName = sessionManager.getUserName();
        int avatarId = sessionManager.getAvatarId();

        if (studentName == null || studentName.isEmpty()) {
            studentName = "Student"; // Default if not available
        }

        // Create review request
        ReviewRequest reviewRequest = new ReviewRequest(
                tutorId,
                bookingId > 0 ? bookingId : null, // null if bookingId not provided
                studentName,
                avatarId > 0 ? avatarId : null,   // null if avatarId not available
                reviewContent,
                selectedRating
        );

        // Disable submit button to prevent double submission
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        // Make API call
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<ReviewResponse> call = apiService.createReview(reviewRequest);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Submit Review");

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Review submitted successfully: " + response.body().getId());
                    Toast.makeText(RateSessionActivity.this,
                            "Review submitted! ⭐ " + selectedRating,
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "Failed to submit review: " + response.code() + " - " + response.message());
                    Toast.makeText(RateSessionActivity.this,
                            "Failed to submit review: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Submit Review");

                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(RateSessionActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
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

