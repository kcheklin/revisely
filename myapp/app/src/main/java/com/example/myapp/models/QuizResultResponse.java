package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizResultResponse {
    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @SerializedName("id")
        private int id;

        @SerializedName("score")
        private double score;

        @SerializedName("totalQuestions")
        private int totalQuestions;

        @SerializedName("correctAnswers")
        private int correctAnswers;

        @SerializedName("passed")
        private boolean passed;

        @SerializedName("timeTaken")
        private int timeTaken;

        @SerializedName("answers")
        private List<GradedAnswer> answers;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public void setCorrectAnswers(int correctAnswers) {
            this.correctAnswers = correctAnswers;
        }

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }

        public int getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(int timeTaken) {
            this.timeTaken = timeTaken;
        }

        public List<GradedAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<GradedAnswer> answers) {
            this.answers = answers;
        }
    }

    public static class GradedAnswer {
        @SerializedName("questionId")
        private int questionId;

        @SerializedName("userAnswer")
        private String userAnswer;

        @SerializedName("correctAnswer")
        private String correctAnswer;

        @SerializedName("isCorrect")
        private boolean isCorrect;

        @SerializedName("points")
        private int points;

        @SerializedName("explanation")
        private String explanation;

        // Getters and Setters
        public int getQuestionId() {
            return questionId;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public void setCorrect(boolean correct) {
            isCorrect = correct;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }
}

