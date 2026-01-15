package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class BookingRequest {
    @SerializedName("tutorId")
    private int tutorId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("studentName")
    private String studentName;

    @SerializedName("subject")
    private String subject;

    @SerializedName("date")
    private String date; // Format: YYYY-MM-DD

    @SerializedName("time")
    private String time; // Format: HH:mm:ss

    @SerializedName("notes")
    private String notes; // Optional notes field for frontend use

    // Constructor
    public BookingRequest(int tutorId, Integer userId, String studentName, String subject, String date, String time) {
        this.tutorId = tutorId;
        this.userId = userId;
        this.studentName = studentName;
        this.subject = subject;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

