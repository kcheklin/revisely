package com.example.myapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivBack, ivProfile, ivEditPhoto;
    private EditText etName, etField, etBio, etGoals;
    private Button btnSave;
    private TextView tvAddSubject;
    private LinearLayout subjectsContainer;
    private NestedScrollView nestedScrollView;

    private ArrayList<String> selectedSubjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        try {
            initializeViews();
            setupClickListeners();
            loadCurrentData();
            setupKeyboardScrollFix();

            // Hardcode subjects for testing
            addHardcodedSubjects();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        ivBack = findViewById(R.id.ivBack);
        ivProfile = findViewById(R.id.ivProfile);
        ivEditPhoto = findViewById(R.id.ivEditPhoto);
        etName = findViewById(R.id.etName);
        etField = findViewById(R.id.etField);
        etBio = findViewById(R.id.etBio);
        etGoals = findViewById(R.id.etGoals);
        btnSave = findViewById(R.id.btnSave);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        // Get the subjects container
        subjectsContainer = findViewById(R.id.chipGroupSubjects);

        // Make sure it's set up to wrap content
        if (subjectsContainer != null) {
            subjectsContainer.setOrientation(LinearLayout.HORIZONTAL);
        }

        tvAddSubject = findViewById(R.id.tvAddSubject);
    }

    private void setupClickListeners() {
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        if (ivEditPhoto != null) {
            ivEditPhoto.setOnClickListener(v -> {
                Toast.makeText(this, "Image picker - to be implemented", Toast.LENGTH_SHORT).show();
            });
        }

        if (tvAddSubject != null) {
            tvAddSubject.setOnClickListener(v -> showAddSubjectDialog());
        }

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> saveProfile());
        }
    }

    private void loadCurrentData() {
        try {
            // Hardcoded data for testing
            String name = "Jaelyn";
            String field = "Computer Science (AI)";
            String bio = "Coffee☕ and a good study playlist 🎵keep me going.";
            String goals = "• Study 8 hours daily.\n• Score CGPA 4.0\n• Complete all assignments on time.";

            if (etName != null) etName.setText(name);
            if (etField != null) etField.setText(field);
            if (etBio != null) etBio.setText(bio);
            if (etGoals != null) etGoals.setText(goals);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading profile data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupKeyboardScrollFix() {
        if (nestedScrollView == null || etGoals == null) return;

        etGoals.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.postDelayed(() -> {
                    if (nestedScrollView != null && etGoals != null) {
                        nestedScrollView.smoothScrollTo(0, etGoals.getBottom());
                    }
                }, 300);
            }
        });
    }

    private void addHardcodedSubjects() {
        // Clear existing subjects first
        if (subjectsContainer != null) {
            subjectsContainer.removeAllViews();
            selectedSubjects.clear();
        }

        // Add hardcoded subjects
        String[] hardcodedSubjects = {"Biology", "English", "Math", "Chemistry"};
        for (String subject : hardcodedSubjects) {
            addSubjectChip(subject);
        }
    }

    private void addSubjectChip(String subject) {
        try {
            if (selectedSubjects.contains(subject)) {
                Toast.makeText(this, "Subject already added", Toast.LENGTH_SHORT).show();
                return;
            }

            if (subjectsContainer == null) {
                Toast.makeText(this, "Container not initialized", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a custom chip-like TextView
            LinearLayout chipLayout = new LinearLayout(this);
            LinearLayout.LayoutParams chipParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            chipParams.setMargins(0, 0, 16, 16);
            chipLayout.setLayoutParams(chipParams);
            chipLayout.setOrientation(LinearLayout.HORIZONTAL);
            chipLayout.setBackgroundResource(R.drawable.pf_subject_chip);
            chipLayout.setPadding(24, 12, 16, 12);
            chipLayout.setGravity(Gravity.CENTER_VERTICAL);

            // Subject text
            TextView textView = new TextView(this);
            textView.setText(subject);
            textView.setTextSize(14);
            textView.setTextColor(Color.parseColor("#000000"));

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(textParams);

            // Close button
            TextView closeButton = new TextView(this);
            closeButton.setText(" ✕");
            closeButton.setTextSize(16);
            closeButton.setTextColor(Color.parseColor("#666666"));
            closeButton.setPadding(8, 0, 0, 0);

            LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            closeButton.setLayoutParams(closeParams);

            closeButton.setOnClickListener(v -> {
                subjectsContainer.removeView(chipLayout);
                selectedSubjects.remove(subject);
            });

            chipLayout.addView(textView);
            chipLayout.addView(closeButton);

            subjectsContainer.addView(chipLayout);
            selectedSubjects.add(subject);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error adding subject: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddSubjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        EditText etSubjectName = dialogView.findViewById(R.id.etSubjectName);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnAdd = dialogView.findViewById(R.id.btnAddDialog);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            try {
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.pf_dialog_background);
            } catch (Exception e) {
                // Use default background if drawable not found
            }
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
        }

        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                if (etSubjectName != null) {
                    String subjectName = etSubjectName.getText().toString().trim();

                    if (subjectName.isEmpty()) {
                        Toast.makeText(this, "Please enter subject name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addSubjectChip(subjectName);
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }

    private void saveProfile() {
        if (etName == null) {
            Toast.makeText(this, "Error: Name field not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String field = etField != null ? etField.getText().toString().trim() : "";
        String bio = etBio != null ? etBio.getText().toString().trim() : "";
        String goals = etGoals != null ? etGoals.getText().toString().trim() : "";
        String subjects = String.join(",", selectedSubjects);

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", name);
        editor.putString("field", field);
        editor.putString("bio", bio);
        editor.putString("goals", goals);
        editor.putString("subjects", subjects);

        editor.apply();

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}