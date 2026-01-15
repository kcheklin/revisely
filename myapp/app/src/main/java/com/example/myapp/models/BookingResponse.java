package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class BookingResponse {
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
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("error")
    private String error;

    // Getters
    public int getId() {
        return id;
    }

    public int getTutorId() {
        return tutorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
}

