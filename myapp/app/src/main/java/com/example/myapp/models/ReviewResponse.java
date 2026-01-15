package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class ReviewResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("tutorId")
    private int tutorId;

    @SerializedName("bookingId")
    private Integer bookingId;

    @SerializedName("studentName")
    private String studentName;

    @SerializedName("avatarId")
    private Integer avatarId;

    @SerializedName("content")
    private String content;

    @SerializedName("rating")
    private int rating;

    @SerializedName("likes")
    private int likes;

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

    public Integer getBookingId() {
        return bookingId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

    public int getLikes() {
        return likes;
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

