package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;

import java.util.*;
import androidx.annotation.*;

public class TutorDiscoveryFragment extends Fragment {
    public static List<Tutor> tutorList; //mock tutor list

    public TutorDiscoveryFragment(){
        super(R.layout.fragment_tutor_discovery);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancesState){
        super.onViewCreated(view, savedInstancesState);

        ChipGroup cpSubject = view.findViewById(R.id.CGDiscoverySubject);
        ChipGroup cpRating = view.findViewById(R.id.CGDiscoveryRating);
        RecyclerView rvDiscovery = view.findViewById(R.id.RVDiscovery);
        LinearLayout navHome = view.findViewById(R.id.navHome);
        LinearLayout navSearch = view.findViewById(R.id.navSearch);
        LinearLayout navUpcoming = view.findViewById(R.id.navUpcoming);
        LinearLayout navProfile = view.findViewById(R.id.navProfile);

        //set default selected filter
        cpSubject.check(R.id.CpSubjectAll);
        cpRating.check(R.id.CpRatingAll);

        rvDiscovery.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch tutors from API
        com.example.myapp.api.RetrofitClient.getApiService().getTutors().enqueue(new retrofit2.Callback<List<Tutor>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Tutor>> call, retrofit2.Response<List<Tutor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tutorList = response.body();
                    TutorAdapter tutorAdapter = new TutorAdapter(getContext(), tutorList);
                    rvDiscovery.setAdapter(tutorAdapter);
                } else {
                    // Fallback to mock data if API fails
                    loadMockData();
                    TutorAdapter tutorAdapter = new TutorAdapter(getContext(), tutorList);
                    rvDiscovery.setAdapter(tutorAdapter);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Tutor>> call, Throwable t) {
                // Handle error - load mock data as fallback
                t.printStackTrace();
                loadMockData();
                TutorAdapter tutorAdapter = new TutorAdapter(getContext(), tutorList);
                rvDiscovery.setAdapter(tutorAdapter);
            }
        });

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StudentHomepageActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        navSearch.setOnClickListener(v -> {
            //in search page already
        });

        navUpcoming.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpcomingSessionsActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void loadMockData() {
        if(tutorList == null){
            tutorList = new ArrayList<>();
            //mock tutor list, can change later
            tutorList.add(new Tutor(
                    "1",
                    "Dr. Emily Chen",
                    "Engineering",
                    Arrays.asList("Mathematics", "Physics", "Additional Mathematics"),
                    R.drawable.avatar_tutor_emily,
                    "Mathematics & Physics Expert",
                    "With over 10 years of teaching experience, I specialize in making complex mathematical and physics concepts easy to understand. I've helped hundreds of students improve their grades and develop a genuine love for STEM subjects. My teaching approach focuses on building strong fundamentals while encouraging critical thinking and problem-solving skills.",
                    127,
                    450,
                    10,
                    Arrays.asList(108, 15, 3, 1, 0),
                    Arrays.asList("Beginner", "Intermediate", "Intermediate"),
                    Arrays.asList("Advanced", "Advanced", "Intermediate"),
                    Arrays.asList("Ph.D. in Mathematics", "B.Sc. in Applied Mathematics"),
                    Arrays.asList("Stanford University", "UC Berkeley"),
                    Arrays.asList(2015, 2009),
                    Arrays.asList("2025-12-01 9:00", "2025-12-01 10:00", "2025-12-01 11:00", "2025-12-01 14:00", "2025-12-01 15:00", "2025-12-01 16:00", "2025-12-02 10:00", "2025-12-02 20:00"),
                    Arrays.asList(true, true, false, true, true, true, true, false),
                    Arrays.asList("Jessica", "Michael"),
                    Arrays.asList(R.drawable.avatar_student_jessica, R.drawable.avatar_student_michael),
                    Arrays.asList("Excellent tutor! Very patient and explains concepts clearly. I went from struggling with calculus to actually enjoying it. Highly recommend Dr. Chen to anyone looking for a math tutor!", "Dr. Chen is amazing! She helped me prepare for my physics exam and I got an A. Her teaching style is clear and she makes complex topics easy to understand."),
                    Arrays.asList(5, 4),
                    Arrays.asList(12, 8)));
            tutorList.add(new Tutor(
                    "2",
                    "Prof. Marcus Johnson",
                    "Computer Science and Information Technology",
                    Arrays.asList("Database", "Data Structure"),
                    R.drawable.avatar_tutor_marcus,
                    "Computer Science Expert",
                    "Expert in database systems and data structures with a strong focus on practical, hands-on learning.",
                    90,
                    380,
                    7,
                    Arrays.asList(88, 14, 2, 1, 0),
                    Arrays.asList("Intermediate", "Beginner"),
                    Arrays.asList("Advanced", "Advanced"),
                    Arrays.asList("M.Sc in Computer Science"),
                    Arrays.asList("MIT"),
                    Arrays.asList(2012),
                    Arrays.asList("2025-11-30 10:00"),
                    Arrays.asList(true),
                    Arrays.asList("Sarah"),
                    Arrays.asList(R.drawable.avatar_student_jessica),
                    Arrays.asList("The tutor explains every concept clearly and makes difficult topics easy to understand. Always patient and supportive!"),
                    Arrays.asList(5),
                    Arrays.asList(142)));
        }
    }
}