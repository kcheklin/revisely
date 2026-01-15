package com.example.myapp.models;

public class AnalyticsData {
    private String subject;
    private int minutes;
    private double hours;

    public String getSubject() {
        return subject;
    }

    public int getMinutes() {
        return minutes;
    }

    public double getHours() {
        return hours;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
}

