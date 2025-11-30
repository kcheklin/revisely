package com.example.myapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class QuestionActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView timerText, questionNumber, questionText;
    private TextView option1, option2, option3, option4;
    private Button nextButton;
    private ProgressBar timerProgress;
    private CountDownTimer countDownTimer;

    private int selectedOption = -1;
    private int currentQuestion = 1;  // Changed from 7 to 1
    private int totalQuestions = 10;
    private int timeLeft = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        timerText = findViewById(R.id.timerText);
        questionNumber = findViewById(R.id.questionNumber);
        questionText = findViewById(R.id.questionText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        timerProgress = findViewById(R.id.timerProgress);

        // Set question number
        questionNumber.setText(currentQuestion + " /" + totalQuestions);

        // Start timer
        startTimer();

        // Setup option click listeners
        setupOptionListeners();

        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOption != -1) {
                    // User has selected an option, proceed to next
                    Intent intent = new Intent(QuestionActivity.this, QuizResultActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Optional: Show a message that user must select an option
                    // Toast.makeText(QuestionActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupOptionListeners() {
        option1.setOnClickListener(v -> selectOption(1));
        option2.setOnClickListener(v -> selectOption(2));
        option3.setOnClickListener(v -> selectOption(3));
        option4.setOnClickListener(v -> selectOption(4));
    }

    private void selectOption(int optionNumber) {
        // Reset all options first
        resetOptions();

        // Set the selected option
        selectedOption = optionNumber;

        TextView selectedTextView = null;
        switch (optionNumber) {
            case 1: selectedTextView = option1; break;
            case 2: selectedTextView = option2; break;
            case 3: selectedTextView = option3; break;
            case 4: selectedTextView = option4; break;
        }

        if (selectedTextView != null) {
            // Set selected background
            selectedTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.qz_option_selected));

            // Set white text color
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.white));

            // Set bold text style
            selectedTextView.setTypeface(null, Typeface.BOLD);

            // Add check icon at the end
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.qz_ic_check, 0);
        }
    }

    private void resetOptions() {
        TextView[] options = {option1, option2, option3, option4};
        for (TextView option : options) {
            // Reset to unselected background
            option.setBackground(ContextCompat.getDrawable(this, R.drawable.qz_option_unselected));

            // Reset to dark text color
            option.setTextColor(ContextCompat.getColor(this, R.color.text_dark));

            // Reset to normal text style (not bold)
            option.setTypeface(null, Typeface.NORMAL);

            // Remove any drawable icons
            option.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    private void startTimer() {
        // Timer counts down from 30 seconds (30000 milliseconds)
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update time left in seconds
                timeLeft = (int) (millisUntilFinished / 1000);
                timerText.setText(String.valueOf(timeLeft));

                // Calculate progress: starts at 100, decreases to 0
                // This makes the green circle shrink as time runs out
                int progress = (int) ((millisUntilFinished / 30000.0) * 100);
                timerProgress.setProgress(progress);
            }

            @Override
            public void onFinish() {
                // When timer finishes
                timerText.setText("0");
                timerProgress.setProgress(0);  // Circle becomes fully gray

                // Optional: Auto-submit the answer or show time's up message
                // You can add your logic here
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}