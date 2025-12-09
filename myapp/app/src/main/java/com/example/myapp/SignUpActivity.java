package com.example.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

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
                successDialog.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        });
    }
}