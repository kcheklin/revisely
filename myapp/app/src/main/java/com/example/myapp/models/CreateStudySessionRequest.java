package com.example.myapp.models;

public class CreateStudySessionRequest {
    private String subject;
    private int durationInMinutes;
    private String startTime;

    public CreateStudySessionRequest(String subject, int durationInMinutes, String startTime) {
        this.subject = subject;
        this.durationInMinutes = durationInMinutes;
        this.startTime = startTime;
    }

    // Getters
    public String getSubject() {
        return subject;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getStartTime() {
        return startTime;
    }

    // Setters
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

