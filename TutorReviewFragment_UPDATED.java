package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.ReviewResponse;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorReviewFragment extends Fragment {

    private Tutor tutor;
    private RecyclerView rvReview;
    private ReviewAdapter reviewAdapter;
    private List<Review> allReviews = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvNoReviews;

    public TutorReviewFragment() {
        super(R.layout.fragment_tutor_review);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get tutor ID from arguments
        String tutorId = getArguments().getString("tutorId");
        
        // Find tutor in static list (for displaying tutor info)
        tutor = findTutorByTutorId(tutorId);

        if (tutor == null) {
            Toast.makeText(getContext(), "Tutor not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize views
        ImageView ivAvatar = view.findViewById(R.id.IVAvatarReview);
        TextView tvName = view.findViewById(R.id.TVTutorNameReview);
        TextView tvRole = view.findViewById(R.id.TVTutorRoleReview);
        TextView tvRating = view.findViewById(R.id.TVRatingReview);
        TextView tvReviews = view.findViewById(R.id.TVReviews2);
        ProgressBar[] pbStars = {
                view.findViewById(R.id.PB5StarReview),
                view.findViewById(R.id.PB4StarReview),
                view.findViewById(R.id.PB3StarReview),
                view.findViewById(R.id.PB2StarReview),
                view.findViewById(R.id.PB1StarReview)
        };
        TextView[] tvStarsCount = {
                view.findViewById(R.id.TV5StarReview),
                view.findViewById(R.id.TV4StarReview),
                view.findViewById(R.id.TV3StarReview),
                view.findViewById(R.id.TV2StarReview),
                view.findViewById(R.id.TV1StarReview)
        };
        Chip[] cpStars = {
                view.findViewById(R.id.CPAllReview),
                view.findViewById(R.id.CP5StarsReview),
                view.findViewById(R.id.CP4StarsReview),
                view.findViewById(R.id.CP3StarsReview),
                view.findViewById(R.id.CP2StarsReview),
                view.findViewById(R.id.CP1StarsReview)
        };
        ChipGroup cgRating = view.findViewById(R.id.CGReviewRating);

        rvReview = view.findViewById(R.id.RVReview);
        progressBar = view.findViewById(R.id.progressBar); // Add to layout
        tvNoReviews = view.findViewById(R.id.tvNoReviews); // Add to layout

        // Display tutor information
        ivAvatar.setImageResource(tutor.getAvatarId());
        tvName.setText(tutor.getName());
        tvRole.setText(tutor.getRole());
        tvRating.setText(String.format("%.1f", tutor.getAverageRating()));
        tvReviews.setText(tutor.getTotalRatings() + " reviews");

        // Setup star rating distribution
        cpStars[0].setText("All Stars (" + tutor.getTotalRatings() + ")");
        for (int i = 0; i < 5; i++) {
            cpStars[i + 1].setText(5 - i + " Stars (" + tutor.getStarRatings().get(i) + ")");
            int percent = tutor.getTotalRatings() == 0 ? 0 : (tutor.getStarRatings().get(i) * 100 / tutor.getTotalRatings());
            pbStars[i].setProgress(percent);
            tvStarsCount[i].setText(String.valueOf(tutor.getStarRatings().get(i)));
        }

        // Setup RecyclerView
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewAdapter = new ReviewAdapter(new ArrayList<>());
        rvReview.setAdapter(reviewAdapter);

        // Load reviews from backend
        loadReviewsFromBackend(Integer.parseInt(tutorId));

        // Setup chip filtering
        cgRating.check(R.id.CPAllReview);
        cgRating.setOnCheckedStateChangeListener((group, checkedIds) -> {
            filterReviews(checkedIds, cpStars);
        });

        // Back button
        view.findViewById(R.id.IVBackReview).setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void loadReviewsFromBackend(int tutorId) {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        rvReview.setVisibility(View.GONE);
        tvNoReviews.setVisibility(View.GONE);

        // Make API call
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(requireContext());
        Call<List<ReviewResponse>> call = apiService.getReviews(tutorId);

        call.enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewResponse> reviewResponses = response.body();

                    if (reviewResponses.isEmpty()) {
                        tvNoReviews.setVisibility(View.VISIBLE);
                        tvNoReviews.setText("No reviews yet");
                        rvReview.setVisibility(View.GONE);
                    } else {
                        // Convert ReviewResponse to Review objects
                        allReviews.clear();
                        for (ReviewResponse reviewResponse : reviewResponses) {
                            Review review = new Review(
                                    reviewResponse.getAvatarId() != null ? reviewResponse.getAvatarId() : R.drawable.ic_avatar_default,
                                    reviewResponse.getStudentName(),
                                    reviewResponse.getContent(),
                                    reviewResponse.getRating(),
                                    reviewResponse.getLikes()
                            );
                            allReviews.add(review);
                        }

                        // Update RecyclerView
                        rvReview.setVisibility(View.VISIBLE);
                        reviewAdapter.updateList(allReviews);
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Failed to load reviews: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                    tvNoReviews.setVisibility(View.VISIBLE);
                    tvNoReviews.setText("Failed to load reviews");
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvNoReviews.setVisibility(View.VISIBLE);
                tvNoReviews.setText("Network error. Please try again.");

                Toast.makeText(getContext(),
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterReviews(List<Integer> checkedIds, Chip[] cpStars) {
        List<Review> filteredList = new ArrayList<>();

        if (!checkedIds.isEmpty()) {
            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.CPAllReview) {
                // Show all reviews
                filteredList.addAll(allReviews);
            } else {
                // Filter by star rating
                int selectedStar;
                if (checkedId == R.id.CP5StarsReview) {
                    selectedStar = 5;
                } else if (checkedId == R.id.CP4StarsReview) {
                    selectedStar = 4;
                } else if (checkedId == R.id.CP3StarsReview) {
                    selectedStar = 3;
                } else if (checkedId == R.id.CP2StarsReview) {
                    selectedStar = 2;
                } else {
                    selectedStar = 1;
                }

                for (Review review : allReviews) {
                    if (review.getStar() == selectedStar) {
                        filteredList.add(review);
                    }
                }
            }
        }

        reviewAdapter.updateList(filteredList);
    }

    private Tutor findTutorByTutorId(String tutorId) {
        if (TutorDiscoveryFragment.tutorList != null) {
            for (Tutor tutor : TutorDiscoveryFragment.tutorList) {
                if (tutor.getTutorId().equals(tutorId)) {
                    return tutor;
                }
            }
        }
        return null;
    }
}

