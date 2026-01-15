package com.example.myapp.models;

import java.util.List;

public class AnalyticsSummary {
    private int totalMinutes;
    private double totalHours;
    private int totalSessions;
    private int recentSessions;
    private List<SubjectAnalytics> bySubject;

    // Getters
    public int getTotalMinutes() {
        return totalMinutes;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public int getRecentSessions() {
        return recentSessions;
    }

    public List<SubjectAnalytics> getBySubject() {
        return bySubject;
    }

    // Setters
    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public void setRecentSessions(int recentSessions) {
        this.recentSessions = recentSessions;
    }

    public void setBySubject(List<SubjectAnalytics> bySubject) {
        this.bySubject = bySubject;
    }

    public static class SubjectAnalytics {
        private String subject;
        private int totalMinutes;
        private int sessionCount;

        public String getSubject() {
            return subject;
        }

        public int getTotalMinutes() {
            return totalMinutes;
        }

        public int getSessionCount() {
            return sessionCount;
        }

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
}

