package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class Booking {
    @SerializedName("id")
    private int id;

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

    @SerializedName("status")
    private String status; // "Upcoming", "Completed", "Cancelled"

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    // Constructors
    public Booking() {}

    public Booking(int tutorId, Integer userId, String studentName, String subject, String date, String time) {
        this.tutorId = tutorId;
        this.userId = userId;
        this.studentName = studentName;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.status = "Upcoming";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

