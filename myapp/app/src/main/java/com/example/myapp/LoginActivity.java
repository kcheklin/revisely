package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.ErrorResponse;
import com.example.myapp.models.LoginRequest;
import com.example.myapp.models.LoginResponse;
import com.example.myapp.utils.SessionManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvSignUp;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToHome();
            return;
        }

        etEmailUsername = findViewById(R.id.etEmailUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        String email = etEmailUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
        if (email.isEmpty()) {
            etEmailUsername.setError("Email is required");
            etEmailUsername.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailUsername.setError("Please enter a valid email");
            etEmailUsername.requestFocus();
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

        // Disable login button to prevent multiple clicks
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        // Make API call
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = RetrofitClient.getApiService().login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    // Save user session
                    sessionManager.createLoginSession(
                            loginResponse.getToken(),
                            loginResponse.getRefreshToken(),
                            loginResponse.getUser().getId(),
                            loginResponse.getUser().getName(),
                            loginResponse.getUser().getEmail(),
                            loginResponse.getUser().getRole()
                    );

                    Toast.makeText(LoginActivity.this, "Welcome, " + loginResponse.getUser().getName() + "!", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                } else {
                    // Handle error response
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Gson gson = new Gson();
                            ErrorResponse errorResponse = gson.fromJson(errorBody, ErrorResponse.class);
                            Toast.makeText(LoginActivity.this, errorResponse.getError(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, StudentHomepageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}