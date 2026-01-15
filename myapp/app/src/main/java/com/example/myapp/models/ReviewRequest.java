package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {
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
    private int rating; // 1-5

    // Constructor
    public ReviewRequest(int tutorId, Integer bookingId, String studentName, Integer avatarId, String content, int rating) {
        this.tutorId = tutorId;
        this.bookingId = bookingId;
        this.studentName = studentName;
        this.avatarId = avatarId;
        this.content = content;
        this.rating = rating;
    }

    // Getters and Setters
    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

