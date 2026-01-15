package com.example.myapp.models;

public class StudySession {
    private int id;
    private int userId;
    private String subject;
    private int durationInMinutes;
    private String startTime;
    private String endTime;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public StudySession() {
    }

    public StudySession(String subject, int durationInMinutes, String startTime) {
        this.subject = subject;
        this.durationInMinutes = durationInMinutes;
        this.startTime = startTime;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getSubject() {
        return subject;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

