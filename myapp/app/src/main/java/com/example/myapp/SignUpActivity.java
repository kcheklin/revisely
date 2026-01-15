package com.example.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.ErrorResponse;
import com.example.myapp.models.SignupRequest;
import com.example.myapp.models.SignupResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnPrimaryStudent, btnSecondaryStudent, btnSignUp;
    private LinearLayout successDialog;
    private boolean isPrimarySelected = false;
    private boolean isSecondarySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnPrimaryStudent = findViewById(R.id.btnPrimaryStudent);
        btnSecondaryStudent = findViewById(R.id.btnSecondaryStudent);
        btnSignUp = findViewById(R.id.btnSignUp);
        successDialog = findViewById(R.id.successDialog);

        btnPrimaryStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrimarySelected = true;
                isSecondarySelected = false;
                btnPrimaryStudent.setBackgroundResource(R.drawable.btn_selected);
                btnPrimaryStudent.setTextColor(Color.WHITE);
                btnSecondaryStudent.setBackgroundResource(R.drawable.btn_unselected);
                btnSecondaryStudent.setTextColor(Color.parseColor("#3D3228"));
            }
        });

        btnSecondaryStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSecondarySelected = true;
                isPrimarySelected = false;
                btnSecondaryStudent.setBackgroundResource(R.drawable.btn_selected);
                btnSecondaryStudent.setTextColor(Color.WHITE);
                btnPrimaryStudent.setBackgroundResource(R.drawable.btn_unselected);
                btnPrimaryStudent.setTextColor(Color.parseColor("#3D3228"));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });
    }

    private void performSignup() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (fullName.length() < 2) {
            etFullName.setError("Name must be at least 2 characters");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!isPrimarySelected && !isSecondarySelected) {
            Toast.makeText(this, "Please select your student level", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable signup button to prevent multiple clicks
        btnSignUp.setEnabled(false);
        btnSignUp.setText("Creating Account...");

        // Determine role based on selection (you can customize this)
        String role = "student"; // Backend expects "student", "tutor", or "admin"

        // Make API call
        SignupRequest signupRequest = new SignupRequest(email, password, fullName, role);
        Call<SignupResponse> call = RetrofitClient.getApiService().signup(signupRequest);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                btnSignUp.setEnabled(true);
                btnSignUp.setText("Sign Up");

                if (response.isSuccessful() && response.body() != null) {
                    SignupResponse signupResponse = response.body();
                    
                    // Show success dialog
                    successDialog.setVisibility(View.VISIBLE);
                    Toast.makeText(SignUpActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to login after delay
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, 1500);
                } else {
                    // Handle error response
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Gson gson = new Gson();
                            ErrorResponse errorResponse = gson.fromJson(errorBody, ErrorResponse.class);
                            Toast.makeText(SignUpActivity.this, errorResponse.getError(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Signup failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Signup failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                btnSignUp.setEnabled(true);
                btnSignUp.setText("Sign Up");
                Toast.makeText(SignUpActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}