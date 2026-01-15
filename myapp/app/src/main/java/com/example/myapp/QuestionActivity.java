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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapp.models.Quiz;
import com.example.myapp.models.QuizQuestion;
import com.example.myapp.models.QuizResultResponse;
import com.example.myapp.models.QuizSubmitRequest;
import com.example.myapp.repositories.QuizRepository;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView timerText, questionNumber, questionText;
    private TextView option1, option2, option3, option4;
    private Button nextButton;
    private ProgressBar timerProgress;
    private CountDownTimer countDownTimer;

    private int selectedOption = -1;
    private int currentQuestionIndex = 0;
    private int timeLeft = 30;
    private long startTime;
    
    private QuizRepository quizRepository;
    private Quiz currentQuiz;
    private List<QuizQuestion> questions;
    private List<QuizSubmitRequest.AnswerSubmission> userAnswers;

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

        quizRepository = new QuizRepository(this);
        userAnswers = new ArrayList<>();
        startTime = System.currentTimeMillis();

        // Get quiz ID from intent
        int quizId = getIntent().getIntExtra("quizId", -1);
        if (quizId == -1) {
            Toast.makeText(this, "Invalid quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load quiz from backend
        loadQuiz(quizId);

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
                    saveAnswer();
                    proceedToNextQuestion();
                } else {
                    Toast.makeText(QuestionActivity.this, 
                        "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadQuiz(int quizId) {
        quizRepository.getQuiz(quizId, new QuizRepository.QuizCallback() {
            @Override
            public void onSuccess(Quiz quiz) {
                currentQuiz = quiz;
                questions = quiz.getQuizQuestions();
                
                if (questions != null && !questions.isEmpty()) {
                    // Set timer based on quiz time limit
                    timeLeft = quiz.getTimeLimit() != null ? quiz.getTimeLimit() * 60 : 1800;
                    displayQuestion(0);
                    startTimer();
                } else {
                    Toast.makeText(QuestionActivity.this, 
                        "No questions available", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(QuestionActivity.this, 
                    "Error loading quiz: " + message, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void displayQuestion(int index) {
        if (questions == null || index >= questions.size()) {
            return;
        }

        currentQuestionIndex = index;
        QuizQuestion question = questions.get(index);
        
        // Update question number
        questionNumber.setText((index + 1) + " /" + questions.size());
        
        // Update question text
        questionText.setText(question.getQuestionText());
        
        // Update options
        List<String> options = question.getOptions();
        if (options != null && options.size() >= 4) {
            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));
            
            // Show all options
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
            option4.setVisibility(View.VISIBLE);
        } else if (options != null && options.size() == 2) {
            // True/False question
            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setVisibility(View.GONE);
            option4.setVisibility(View.GONE);
        }
        
        // Reset selection
        selectedOption = -1;
        resetOptions();
        
        // Update next button text
        if (index == questions.size() - 1) {
            nextButton.setText("Submit");
        } else {
            nextButton.setText("Next");
        }
    }

    private void saveAnswer() {
        if (questions == null || currentQuestionIndex >= questions.size()) {
            return;
        }

        QuizQuestion question = questions.get(currentQuestionIndex);
        String answer = String.valueOf(selectedOption - 1);  // Convert to 0-indexed
        
        // For true/false questions, convert to string
        if (question.getQuestionType().equals("true-false")) {
            List<String> options = question.getOptions();
            if (options != null && selectedOption - 1 < options.size()) {
                answer = options.get(selectedOption - 1).toLowerCase();
            }
        }
        
        userAnswers.add(new QuizSubmitRequest.AnswerSubmission(question.getId(), answer));
    }

    private void proceedToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            // Move to next question
            displayQuestion(currentQuestionIndex + 1);
        } else {
            // Submit quiz
            submitQuiz();
        }
    }

    private void submitQuiz() {
        // Cancel timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Calculate time taken in seconds
        int timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);
        
        QuizSubmitRequest request = new QuizSubmitRequest(userAnswers, timeTaken);
        
        quizRepository.submitQuiz(currentQuiz.getId(), request, 
            new QuizRepository.QuizResultCallback() {
                @Override
                public void onSuccess(QuizResultResponse.Result result) {
                    // Navigate to results
                    Intent intent = new Intent(QuestionActivity.this, QuizResultActivity.class);
                    intent.putExtra("score", result.getScore());
                    intent.putExtra("totalQuestions", result.getTotalQuestions());
                    intent.putExtra("correctAnswers", result.getCorrectAnswers());
                    intent.putExtra("passed", result.isPassed());
                    intent.putExtra("timeTaken", result.getTimeTaken());
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(QuestionActivity.this, 
                        "Error submitting quiz: " + message, Toast.LENGTH_LONG).show();
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