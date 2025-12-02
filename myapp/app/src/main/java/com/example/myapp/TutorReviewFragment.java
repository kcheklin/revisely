package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;

import android.widget.*;
import androidx.annotation.*;
import androidx.recyclerview.widget.*;
import com.google.android.material.chip.*;
import java.util.*;

public class TutorReviewFragment extends Fragment {

    private Tutor tutor;

    public TutorReviewFragment(){
        super(R.layout.fragment_tutor_review);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String tutorId = getArguments().getString("tutorId");
        tutor = findTutorByTutorId(tutorId);

        ImageView ivAvatar = view.findViewById(R.id.IVAvatarReview);
        TextView tvName = view.findViewById(R.id.TVTutorNameReview);
        TextView tvRole = view.findViewById(R.id.TVTutorRoleReview);
        TextView tvRating = view.findViewById(R.id.TVRatingReview);
        TextView tvReviews = view.findViewById(R.id.TVReviews2);
        ProgressBar[] pbStars = {view.findViewById(R.id.PB5StarReview), view.findViewById(R.id.PB4StarReview), view.findViewById(R.id.PB3StarReview), view.findViewById(R.id.PB2StarReview), view.findViewById(R.id.PB1StarReview)};
        TextView[] tvStarsCount = {view.findViewById(R.id.TV5StarReview), view.findViewById(R.id.TV4StarReview), view.findViewById(R.id.TV3StarReview), view.findViewById(R.id.TV2StarReview), view.findViewById(R.id.TV1StarReview)};
        Chip[] cpStars = {view.findViewById(R.id.CPAllReview), view.findViewById(R.id.CP5StarsReview), view.findViewById(R.id.CP4StarsReview), view.findViewById(R.id.CP3StarsReview), view.findViewById(R.id.CP2StarsReview), view.findViewById(R.id.CP1StarsReview)};
        ChipGroup cgRating = view.findViewById(R.id.CGReviewRating);

        ivAvatar.setImageResource(tutor.getAvatarId());
        tvName.setText(tutor.getName());
        tvRole.setText(tutor.getRole());
        tvRating.setText(String.format("%.1f", tutor.getAverageRating()));
        tvReviews.setText(tutor.getTotalRatings() + " reviews");

        cpStars[0].setText("All Stars (" + tutor.getTotalRatings() + ")");
        for(int i = 0; i < 5; i++){
            cpStars[i + 1].setText(5 - i + " Stars (" + tutor.getStarRatings().get(i) + ")");
            int percent = tutor.getTotalRatings() == 0 ? 0 : (tutor.getStarRatings().get(i) * 100 / tutor.getTotalRatings());
            pbStars[i].setProgress(percent);
            tvStarsCount[i].setText(String.valueOf(tutor.getStarRatings().get(i)));
        }

        List<Review> reviewList = new ArrayList<>();
        for(int i = 0; i < tutor.getReviewName().size(); i++){
            reviewList.add(new Review(tutor.getReviewAvatarId().get(i), tutor.getReviewName().get(i), tutor.getReviewContent().get(i), tutor.getReviewStar().get(i), tutor.getReviewLikes().get(i)));
        }

        RecyclerView rvReview = view.findViewById(R.id.RVReview);
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewAdapter reviewAdapter = new ReviewAdapter(new ArrayList<>(reviewList));
        rvReview.setAdapter(reviewAdapter);

        cgRating.check(R.id.CPAllReview);
        cgRating.setOnCheckedStateChangeListener((group, checkedIds) -> {
            List<Review> filteredList = new ArrayList<>();

            if(!checkedIds.isEmpty()){
                int checkedId = checkedIds.get(0);

                if(checkedId == R.id.CPAllReview){
                    filteredList.addAll(reviewList);
                }else{
                    int selectedStar;
                    if(checkedId == R.id.CP5StarsReview){
                        selectedStar = 5;
                    }else if(checkedId == R.id.CP4StarsReview){
                        selectedStar = 4;
                    }else if(checkedId == R.id.CP3StarsReview){
                        selectedStar = 3;
                    }else if(checkedId == R.id.CP2StarsReview){
                        selectedStar = 2;
                    }else{
                        selectedStar = 1;
                    }

                    for(Review review : reviewList){
                        if(review.getStar() == selectedStar){
                            filteredList.add(review);
                        }
                    }
                }
            }

            reviewAdapter.updateList(filteredList);
        });

        view.findViewById(R.id.IVBackReview).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private Tutor findTutorByTutorId(String tutorId){
        if(TutorDiscoveryFragment.tutorList != null){
            for(Tutor tutor : TutorDiscoveryFragment.tutorList){
                if(tutor.getTutorId().equals(tutorId)){
                    return tutor;
                }
            }
        }
        return null;
    }
}
