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
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Tutor>> call, Throwable t) {
                // Handle error (maybe show a toast or log it)
                t.printStackTrace();
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
}
