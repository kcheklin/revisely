package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizQuestion {
    @SerializedName("id")
    private int id;

    @SerializedName("quizId")
    private int quizId;

    @SerializedName("questionNumber")
    private int questionNumber;

    @SerializedName("questionText")
    private String questionText;

    @SerializedName("questionType")
    private String questionType;

    @SerializedName("options")
    private List<String> options;

    @SerializedName("correctAnswer")
    private String correctAnswer;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("points")
    private int points;

    @SerializedName("difficulty")
    private String difficulty;

    // For tracking user's answer (not from backend)
    private String userAnswer;
    private boolean isCorrect;

    // Constructors
    public QuizQuestion() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}

