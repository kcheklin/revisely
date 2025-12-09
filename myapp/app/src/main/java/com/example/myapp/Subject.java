package com.example.myapp;

public class Subject {

    private String subject;
    private String minLevel;
    private String maxLevel;

    public Subject(String subject, String minLevel, String maxLevel) {
        this.subject = subject;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public String getSubject() {
        return subject;
    }

    public String getMinLevel() {
        return minLevel;
    }

    public String getMaxLevel() {
        return maxLevel;
    }
}
