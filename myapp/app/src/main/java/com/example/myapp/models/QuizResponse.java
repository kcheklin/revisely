package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class QuizResponse {
    @SerializedName("quiz")
    private Quiz quiz;

    @SerializedName("message")
    private String message;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

