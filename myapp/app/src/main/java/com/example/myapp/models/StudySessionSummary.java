package com.example.myapp.models;

public class StudySessionSummary {
    private String subject;
    private int totalMinutes;
    private int sessionCount;

    // Getters
    public String getSubject() {
        return subject;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    // Setters
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
}

