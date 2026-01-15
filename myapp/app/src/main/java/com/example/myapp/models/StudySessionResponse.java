package com.example.myapp.models;

public class StudySessionResponse {
    private StudySession session;
    private String message;

    public StudySession getSession() {
        return session;
    }

    public void setSession(StudySession session) {
        this.session = session;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

