package com.example.myapp.models;

public class SignupResponse {
    private User user;

    public SignupResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

