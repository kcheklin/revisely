package com.example.myapp.models;

public class Profile {
    private int id;
    private int userId;
    private String bio;
    private String profilePicture;
    private int avatarId;
    private String theme;
    private boolean notifications;
    private String createdAt;
    private String updatedAt;
    private User User; // Nested user object from backend

    // Constructors
    public Profile() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public String getTheme() {
        return theme;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return User;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser(User user) {
        User = user;
    }
}

