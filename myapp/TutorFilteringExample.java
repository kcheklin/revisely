package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Example implementation showing how to use backend filters in TutorDiscoveryFragment
 * 
 * This is a reference file demonstrating how to:
 * 1. Apply subject filters from ChipGroup
 * 2. Apply rating filters from ChipGroup
 * 3. Apply date filters (for availability)
 * 4. Combine multiple filters
 * 
 * You can integrate these methods into your existing TutorDiscoveryFragment.java
 */
public class TutorFilteringExample extends Fragment {
    
    private RecyclerView rvDiscovery;
    private TutorAdapter tutorAdapter;
    private ChipGroup chipGroupSubject;
    private ChipGroup chipGroupRating;
    
    private String selectedSubject = null;
    private String selectedRating = null;
    private String selectedDate = null;
    
    public TutorFilteringExample() {
        super(R.layout.fragment_tutor_discovery);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        rvDiscovery = view.findViewById(R.id.RVDiscovery);
        chipGroupSubject = view.findViewById(R.id.CGDiscoverySubject);
        chipGroupRating = view.findViewById(R.id.CGDiscoveryRating);
        
        rvDiscovery.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Set up filter listeners
        setupSubjectFilter();
        setupRatingFilter();
        
        // Load all tutors initially
        loadTutors();
    }
    
    /**
     * Load tutors from backend with current filter settings
     */
    private void loadTutors() {
        com.example.myapp.api.RetrofitClient.getApiService()
            .searchTutors(null, selectedSubject, selectedDate, selectedRating)
            .enqueue(new Callback<List<Tutor>>() {
                @Override
                public void onResponse(Call<List<Tutor>> call, Response<List<Tutor>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Tutor> tutors = response.body();
                        updateTutorList(tutors);
                    } else {
                        Toast.makeText(getContext(), "Failed to load tutors", Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onFailure(Call<List<Tutor>> call, Throwable t) {
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
    }
    
    /**
     * Update RecyclerView with new tutor list
     */
    private void updateTutorList(List<Tutor> tutors) {
        if (tutorAdapter == null) {
            tutorAdapter = new TutorAdapter(getContext(), tutors);
            rvDiscovery.setAdapter(tutorAdapter);
        } else {
            // If your TutorAdapter has an update method, call it here
            // tutorAdapter.updateList(tutors);
            // Otherwise, create a new adapter
            tutorAdapter = new TutorAdapter(getContext(), tutors);
            rvDiscovery.setAdapter(tutorAdapter);
        }
    }
    
    /**
     * Set up subject filter with ChipGroup
     */
    private void setupSubjectFilter() {
        chipGroupSubject.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.CpSubjectAll) {
                selectedSubject = null;
            } else if (checkedId == R.id.CpSubjectMath) {
                selectedSubject = "Mathematics";
            } else if (checkedId == R.id.CpSubjectPhysics) {
                selectedSubject = "Physics";
            } else if (checkedId == R.id.CpSubjectDatabase) {
                selectedSubject = "Database";
            } else if (checkedId == R.id.CpSubjectDataStructure) {
                selectedSubject = "Data Structure";
            }
            // Add more subjects as needed
            
            // Reload tutors with new filter
            loadTutors();
        });
    }
    
    /**
     * Set up rating filter with ChipGroup
     */
    private void setupRatingFilter() {
        chipGroupRating.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.CpRatingAll) {
                selectedRating = null;
            } else if (checkedId == R.id.CpRating4Plus) {
                selectedRating = "4.0";
            } else if (checkedId == R.id.CpRating45Plus) {
                selectedRating = "4.5";
            }
            // Add more rating filters as needed
            
            // Reload tutors with new filter
            loadTutors();
        });
    }
    
    /**
     * Example: Filter tutors by availability date
     * Call this method when user selects a date
     */
    private void filterByDate(String date) {
        // Date should be in format: YYYY-MM-DD
        selectedDate = date;
        loadTutors();
    }
    
    /**
     * Example: Search tutors by name
     * Can be called from a SearchView or EditText
     */
    private void searchByName(String name) {
        com.example.myapp.api.RetrofitClient.getApiService()
            .searchTutors(name, null, null, null)
            .enqueue(new Callback<List<Tutor>>() {
                @Override
                public void onResponse(Call<List<Tutor>> call, Response<List<Tutor>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateTutorList(response.body());
                    }
                }
                
                @Override
                public void onFailure(Call<List<Tutor>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }
    
    /**
     * Example: Get tutors teaching Mathematics who are available on a specific date with rating 4.0+
     */
    private void advancedFilterExample() {
        com.example.myapp.api.RetrofitClient.getApiService()
            .searchTutors(
                null,              // name: any
                "Mathematics",     // subject: Mathematics
                "2025-12-15",     // date: December 15, 2025
                "4.0"             // rating: 4.0 or higher
            )
            .enqueue(new Callback<List<Tutor>>() {
                @Override
                public void onResponse(Call<List<Tutor>> call, Response<List<Tutor>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Tutor> filteredTutors = response.body();
                        updateTutorList(filteredTutors);
                        
                        String message = "Found " + filteredTutors.size() + " tutors";
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onFailure(Call<List<Tutor>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }
    
    /**
     * Clear all filters and show all tutors
     */
    private void clearAllFilters() {
        selectedSubject = null;
        selectedRating = null;
        selectedDate = null;
        
        // Reset chip selections
        chipGroupSubject.check(R.id.CpSubjectAll);
        chipGroupRating.check(R.id.CpRatingAll);
        
        // Reload all tutors
        loadTutors();
    }
}

