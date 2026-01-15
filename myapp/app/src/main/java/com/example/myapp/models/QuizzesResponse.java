package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizzesResponse {
    @SerializedName("quizzes")
    private List<Quiz> quizzes;

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}

