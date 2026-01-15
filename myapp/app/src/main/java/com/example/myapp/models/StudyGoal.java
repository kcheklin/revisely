package com.example.myapp.models;

public class StudyGoal {
    private int id;
    private int userId;
    private String subject;
    private int targetHours;
    private int currentHours;
    private String deadline;
    private boolean completed;
    private String priority; // "high", "medium", "low"
    private String description;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public StudyGoal() {
    }

    public StudyGoal(String subject, int targetHours, String deadline, String priority, String description) {
        this.subject = subject;
        this.targetHours = targetHours;
        this.deadline = deadline;
        this.priority = priority;
        this.description = description;
        this.currentHours = 0;
        this.completed = false;
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

    public int getTargetHours() {
        return targetHours;
    }

    public int getCurrentHours() {
        return currentHours;
    }

    public String getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
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

    public void setTargetHours(int targetHours) {
        this.targetHours = targetHours;
    }

    public void setCurrentHours(int currentHours) {
        this.currentHours = currentHours;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods
    public int getProgressPercentage() {
        if (targetHours == 0) return 0;
        return Math.min(100, (currentHours * 100) / targetHours);
    }

    public int getRemainingHours() {
        return Math.max(0, targetHours - currentHours);
    }
}

