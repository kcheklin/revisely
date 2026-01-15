package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.AnalyticsData;
import com.example.myapp.models.AnalyticsSummary;
import com.example.myapp.models.AnalyticsSummaryResponse;
import com.example.myapp.models.DailyAnalyticsResponse;
import com.example.myapp.models.PeriodAnalyticsResponse;
import com.example.myapp.models.ProfileResponse;
import com.example.myapp.models.QuoteResponse;
import com.example.myapp.utils.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvField, tvBio, tvTotalHabits, tvStreak, tvStudyDays, tvGoals;
    private TextView tvMotivation, tvTotalHours;
    private ImageView ivProfile, ivEdit, ivRefresh;
    private Button btnDaily, btnWeekly, btnMonthly, btnLogout;
    private PieChart pieChart;
    private CardView cvSubjects, cvMotivation, cvAnalytics;

    // Bottom Navigation
    private LinearLayout btnNavHome, btnNavSearch, btnNavUpcoming, btnNavProfile;

    private String[] motivationalQuotes = {
            "\"The only way to do great work is to love what you do.\" - Steve Jobs",
            "\"Success is not final, failure is not fatal: it is the courage to continue that counts.\"",
            "\"Believe you can and you're halfway there.\" - Theodore Roosevelt",
            "\"The future depends on what you do today.\" - Mahatma Gandhi",
            "\"Education is the most powerful weapon which you can use to change the world.\"",
            "\"Don't watch the clock; do what it does. Keep going.\" - Sam Levenson"
    };

    private String currentView = "daily";
    private SessionManager sessionManager;
    private AnalyticsSummary cachedSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        initializeViews();
        setupClickListeners();
        setupBottomNavigation();
        setRandomMotivation();
        
        // Load actual user data from backend
        loadUserProfile();
        loadAnalyticsSummary();

        // Set Profile as selected by default
        selectNavItem(btnNavProfile);
    }

    private void initializeViews() {
        tvName = findViewById(R.id.tvName);
        tvField = findViewById(R.id.tvField);
        tvBio = findViewById(R.id.tvBio);
        tvTotalHabits = findViewById(R.id.tvTotalHabits);
        tvStreak = findViewById(R.id.tvStreak);
        tvStudyDays = findViewById(R.id.tvStudyDays);
        tvMotivation = findViewById(R.id.tvMotivation);
        tvTotalHours = findViewById(R.id.tvTotalHours);
        tvGoals = findViewById(R.id.tvGoals);

        ivProfile = findViewById(R.id.ivProfile);
        ivEdit = findViewById(R.id.ivEdit);
        ivRefresh = findViewById(R.id.ivRefresh);

        btnDaily = findViewById(R.id.btnDaily);
        btnWeekly = findViewById(R.id.btnWeekly);
        btnMonthly = findViewById(R.id.btnMonthly);
        btnLogout = findViewById(R.id.btnLogout);

        pieChart = findViewById(R.id.pieChart);

        cvSubjects = findViewById(R.id.cvSubjects);
        cvMotivation = findViewById(R.id.cvMotivation);
        cvAnalytics = findViewById(R.id.cvAnalytics);

        btnNavHome = findViewById(R.id.navHome);
        btnNavSearch = findViewById(R.id.navSearch);
        btnNavUpcoming = findViewById(R.id.navUpcoming);
        btnNavProfile = findViewById(R.id.navProfile);
    }

    private void setupClickListeners() {
        if (ivEdit != null) {
            ivEdit.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
        }

        if (ivRefresh != null) {
            ivRefresh.setOnClickListener(v -> setRandomMotivation());
        }

        if (btnDaily != null) {
            btnDaily.setOnClickListener(v -> updateAnalytics("daily"));
        }

        if (btnWeekly != null) {
            btnWeekly.setOnClickListener(v -> updateAnalytics("weekly"));
        }

        if (btnMonthly != null) {
            btnMonthly.setOnClickListener(v -> updateAnalytics("monthly"));
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                // Clear user session data
                sessionManager.logout();
                
                SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                // Reset authenticated client
                RetrofitClient.resetAuthenticatedClient();

                // Navigate to Login screen
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setupBottomNavigation() {
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                selectNavItem(btnNavHome);
                Intent intent = new Intent(ProfileActivity.this, StudentHomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }

        if (btnNavSearch != null) {
            btnNavSearch.setOnClickListener(v -> {
                selectNavItem(btnNavSearch);
                Intent intent = new Intent(ProfileActivity.this, TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            });
        }

        if (btnNavUpcoming != null) {
            btnNavUpcoming.setOnClickListener(v -> {
                selectNavItem(btnNavUpcoming);
                Intent intent = new Intent(ProfileActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
            });
        }

        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                selectNavItem(btnNavProfile);
                // Already on Profile page
            });
        }
    }

    private void selectNavItem(LinearLayout selectedItem) {
        // Deselect all items
        btnNavHome.setSelected(false);
        btnNavSearch.setSelected(false);
        btnNavUpcoming.setSelected(false);
        btnNavProfile.setSelected(false);

        // Update text colors for all items
        updateNavItemColor(btnNavHome, false);
        updateNavItemColor(btnNavSearch, false);
        updateNavItemColor(btnNavUpcoming, false);
        updateNavItemColor(btnNavProfile, false);

        // Select the clicked item
        selectedItem.setSelected(true);
        updateNavItemColor(selectedItem, true);
    }

    private void updateNavItemColor(LinearLayout navItem, boolean isSelected) {
        // Get the text view (second child, index 1)
        TextView textView = (TextView) navItem.getChildAt(1);
        if (textView != null) {
            textView.setTextColor(isSelected ?
                    Color.parseColor("#FFFFFF") :
                    Color.parseColor("#99A1AF"));
        }
    }

    private void loadProfileData() {
        // Get user data from SessionManager
        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();
        String role = sessionManager.getUserRole();

        if (tvName != null) {
            tvName.setText(name != null ? name : "User");
        }
        
        if (tvField != null) {
            // Display role with email
            String roleDisplay = role != null ? role.substring(0, 1).toUpperCase() + role.substring(1) : "Student";
            tvField.setText(roleDisplay + " • " + (email != null ? email : ""));
        }

        // Try to load additional profile data from SharedPreferences (in case it's cached)
        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String bio = prefs.getString("bio", "Welcome to your profile!");
        String goals = prefs.getString("goals", "• Set your study goals\n• Track your progress\n• Achieve success!");

        if (tvBio != null) tvBio.setText(bio);
        if (tvGoals != null) tvGoals.setText(goals);
    }

    private void loadUserProfile() {
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<ProfileResponse> call = apiService.getMyProfile();

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    
                    if (profileResponse.getProfile() != null) {
                        // Update UI with profile data
                        if (profileResponse.getProfile().getBio() != null && tvBio != null) {
                            tvBio.setText(profileResponse.getProfile().getBio());
                            
                            // Cache bio in SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
                            prefs.edit().putString("bio", profileResponse.getProfile().getBio()).apply();
                        }

                        // If profile has associated User data, update that too
                        if (profileResponse.getProfile().getUser() != null) {
                            String userName = profileResponse.getProfile().getUser().getName();
                            String userEmail = profileResponse.getProfile().getUser().getEmail();
                            String userRole = profileResponse.getProfile().getUser().getRole();
                            
                            if (tvName != null && userName != null) {
                                tvName.setText(userName);
                            }
                            
                            if (tvField != null && userRole != null) {
                                String roleDisplay = userRole.substring(0, 1).toUpperCase() + userRole.substring(1);
                                tvField.setText(roleDisplay + " • " + userEmail);
                            }
                        }
                    }
                } else {
                    // Fallback to SessionManager data
                    loadProfileData();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                // Fallback to SessionManager data
                loadProfileData();
            }
        });
    }

    private void loadAnalyticsSummary() {
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<AnalyticsSummaryResponse> call = apiService.getAnalyticsSummary();

        call.enqueue(new Callback<AnalyticsSummaryResponse>() {
            @Override
            public void onResponse(Call<AnalyticsSummaryResponse> call, Response<AnalyticsSummaryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedSummary = response.body().getSummary();
                    
                    // Update stats
                    if (tvTotalHabits != null && cachedSummary != null) {
                        tvTotalHabits.setText(String.valueOf(cachedSummary.getTotalSessions()));
                    }
                    
                    if (tvStreak != null && cachedSummary != null) {
                        tvStreak.setText(cachedSummary.getRecentSessions() + "+");
                    }
                    
                    if (tvStudyDays != null && cachedSummary != null) {
                        int days = cachedSummary.getTotalSessions(); // Approximation
                        tvStudyDays.setText(days + " sessions");
                    }

                    // Load the current period analytics
                    updateAnalytics(currentView);
                } else {
                    // Use default data
                    updateAnalytics(currentView);
                }
            }

            @Override
            public void onFailure(Call<AnalyticsSummaryResponse> call, Throwable t) {
                // Use default data
                updateAnalytics(currentView);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
        loadAnalyticsSummary();
        selectNavItem(btnNavProfile);
    }

    private void setRandomMotivation() {
        if (tvMotivation == null) return;

        // Try to fetch from backend first
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        Call<QuoteResponse> call = apiService.getRandomQuote(null); // null = any category

        call.enqueue(new Callback<QuoteResponse>() {
            @Override
            public void onResponse(Call<QuoteResponse> call, Response<QuoteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getQuote() != null) {
                    String quote = response.body().getQuote().getFormattedQuote();
                    tvMotivation.setText(quote);
                } else {
                    // Fallback to local quotes
                    setLocalMotivation();
                }
            }

            @Override
            public void onFailure(Call<QuoteResponse> call, Throwable t) {
                // Fallback to local quotes
                setLocalMotivation();
            }
        });
    }

    private void setLocalMotivation() {
        if (tvMotivation != null) {
            Random random = new Random();
            String quote = motivationalQuotes[random.nextInt(motivationalQuotes.length)];
            tvMotivation.setText(quote);
        }
    }

    private void updateAnalytics(String period) {
        currentView = period;

        if (btnDaily != null) {
            btnDaily.setBackgroundResource(period.equals("daily") ? R.drawable.pf_btn_active : R.drawable.pf_btn_inactive);
        }

        if (btnWeekly != null) {
            btnWeekly.setBackgroundResource(period.equals("weekly") ? R.drawable.pf_btn_active : R.drawable.pf_btn_inactive);
        }

        if (btnMonthly != null) {
            btnMonthly.setBackgroundResource(period.equals("monthly") ? R.drawable.pf_btn_active : R.drawable.pf_btn_inactive);
        }

        // Fetch analytics data from backend
        ApiService apiService = RetrofitClient.getAuthenticatedApiService(this);
        
        if (period.equals("daily")) {
            Call<DailyAnalyticsResponse> call = apiService.getDailyAnalytics(null); // null = today
            call.enqueue(new Callback<DailyAnalyticsResponse>() {
                @Override
                public void onResponse(Call<DailyAnalyticsResponse> call, Response<DailyAnalyticsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        displayAnalytics(response.body().getData());
                    } else {
                        displayDefaultAnalytics(period);
                    }
                }

                @Override
                public void onFailure(Call<DailyAnalyticsResponse> call, Throwable t) {
                    displayDefaultAnalytics(period);
                }
            });
        } else if (period.equals("weekly")) {
            Call<PeriodAnalyticsResponse> call = apiService.getWeeklyAnalytics();
            call.enqueue(new Callback<PeriodAnalyticsResponse>() {
                @Override
                public void onResponse(Call<PeriodAnalyticsResponse> call, Response<PeriodAnalyticsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        displayAnalytics(response.body().getData());
                    } else {
                        displayDefaultAnalytics(period);
                    }
                }

                @Override
                public void onFailure(Call<PeriodAnalyticsResponse> call, Throwable t) {
                    displayDefaultAnalytics(period);
                }
            });
        } else { // monthly
            Call<PeriodAnalyticsResponse> call = apiService.getMonthlyAnalytics();
            call.enqueue(new Callback<PeriodAnalyticsResponse>() {
                @Override
                public void onResponse(Call<PeriodAnalyticsResponse> call, Response<PeriodAnalyticsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        displayAnalytics(response.body().getData());
                    } else {
                        displayDefaultAnalytics(period);
                    }
                }

                @Override
                public void onFailure(Call<PeriodAnalyticsResponse> call, Throwable t) {
                    displayDefaultAnalytics(period);
                }
            });
        }
    }

    private void displayAnalytics(List<AnalyticsData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            // Show message if no data
            if (tvTotalHours != null) {
                tvTotalHours.setText("0 h");
            }
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(1, "No data yet"));
            setupPieChart(entries);
            return;
        }

        // Calculate total hours
        double totalHours = 0;
        for (AnalyticsData data : dataList) {
            totalHours += data.getHours();
        }

        if (tvTotalHours != null) {
            tvTotalHours.setText(String.format("%.1f h", totalHours));
        }

        // Create pie chart entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (AnalyticsData data : dataList) {
            entries.add(new PieEntry((float) data.getHours(), data.getSubject()));
        }

        setupPieChart(entries);
    }

    private void displayDefaultAnalytics(String period) {
        // Fallback to default/sample data
        float totalHours = 0;
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (period.equals("daily")) {
            totalHours = 0.0f;
            entries.add(new PieEntry(1, "Start studying!"));
        } else if (period.equals("weekly")) {
            totalHours = 0.0f;
            entries.add(new PieEntry(1, "No sessions yet"));
        } else {
            totalHours = 0.0f;
            entries.add(new PieEntry(1, "Track your study time"));
        }

        if (tvTotalHours != null) {
            tvTotalHours.setText(totalHours + " h");
        }

        setupPieChart(entries);
    }

    private void setupPieChart(ArrayList<PieEntry> entries) {
        if (pieChart == null) return;

        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255, 204, 102)); // Yellow - Biology
        colors.add(Color.rgb(255, 127, 80));  // Orange - Chemistry
        colors.add(Color.rgb(102, 204, 102)); // Green - Mathematics
        colors.add(Color.rgb(102, 153, 204)); // Blue - English

        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}