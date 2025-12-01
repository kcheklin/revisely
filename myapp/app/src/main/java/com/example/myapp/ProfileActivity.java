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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        setupClickListeners();
        setupBottomNavigation();
        setRandomMotivation();
        updateAnalytics("daily");

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
                SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

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
                Intent intent = new Intent(ProfileActivity.this, UpcomingSessionsFragment.class);
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
        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);

        String name = prefs.getString("name", "Jaelyn");
        String field = prefs.getString("field", "Computer Science (AI)");
        String bio = prefs.getString("bio", "Coffee☕ and a good study playlist 🎵keep me going.");
        String goals = prefs.getString("goals", "• Study 8 hours daily.\n• Score CGPA 4.0\n• Complete all assignments on time.");

        if (tvName != null) tvName.setText(name);
        if (tvField != null) tvField.setText(field);
        if (tvBio != null) tvBio.setText(bio);
        if (tvGoals != null) tvGoals.setText(goals);

        if (tvTotalHabits != null) tvTotalHabits.setText("100");
        if (tvStreak != null) tvStreak.setText("50+");
        if (tvStudyDays != null) tvStudyDays.setText("30 days");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
        selectNavItem(btnNavProfile);
    }

    private void setRandomMotivation() {
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

        float totalHours = 0;
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (period.equals("daily")) {
            totalHours = 8.0f;
            entries.add(new PieEntry(1.5f, "Biology"));
            entries.add(new PieEntry(1.8f, "Chemistry"));
            entries.add(new PieEntry(1.2f, "Mathematics"));
            entries.add(new PieEntry(3.5f, "English"));
        } else if (period.equals("weekly")) {
            totalHours = 44.8f;
            entries.add(new PieEntry(12.5f, "Biology"));
            entries.add(new PieEntry(9.3f, "Chemistry"));
            entries.add(new PieEntry(7.8f, "Mathematics"));
            entries.add(new PieEntry(15.2f, "English"));
        } else {
            totalHours = 186.0f;
            entries.add(new PieEntry(52.3f, "Biology"));
            entries.add(new PieEntry(38.7f, "Chemistry"));
            entries.add(new PieEntry(37.2f, "Mathematics"));
            entries.add(new PieEntry(60.8f, "English"));
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