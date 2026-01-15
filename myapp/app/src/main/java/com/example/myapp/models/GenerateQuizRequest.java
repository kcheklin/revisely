package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

public class GenerateQuizRequest {
    @SerializedName("chapterId")
    private int chapterId;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("numQuestions")
    private int numQuestions;

    @SerializedName("questionTypes")
    private List<String> questionTypes;

    @SerializedName("timeLimit")
    private int timeLimit;

    @SerializedName("passingScore")
    private int passingScore;

    // Constructor
    public GenerateQuizRequest(int chapterId, String difficulty, int numQuestions) {
        this.chapterId = chapterId;
        this.difficulty = difficulty;
        this.numQuestions = numQuestions;
        this.questionTypes = Arrays.asList("multiple-choice", "true-false");
        this.timeLimit = 30;
        this.passingScore = 70;
    }

    // Full constructor
    public GenerateQuizRequest(int chapterId, String difficulty, int numQuestions, 
                              List<String> questionTypes, int timeLimit, int passingScore) {
        this.chapterId = chapterId;
        this.difficulty = difficulty;
        this.numQuestions = numQuestions;
        this.questionTypes = questionTypes;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
    }

    // Getters and Setters
    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public List<String> getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(List<String> questionTypes) {
        this.questionTypes = questionTypes;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }
}

