package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizSubmitRequest {
    @SerializedName("answers")
    private List<AnswerSubmission> answers;

    @SerializedName("timeTaken")
    private int timeTaken;

    public QuizSubmitRequest(List<AnswerSubmission> answers, int timeTaken) {
        this.answers = answers;
        this.timeTaken = timeTaken;
    }

    public List<AnswerSubmission> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmission> answers) {
        this.answers = answers;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public static class AnswerSubmission {
        @SerializedName("questionId")
        private int questionId;

        @SerializedName("answer")
        private String answer;

        public AnswerSubmission(int questionId, String answer) {
            this.questionId = questionId;
            this.answer = answer;
        }

        public int getQuestionId() {
            return questionId;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}

