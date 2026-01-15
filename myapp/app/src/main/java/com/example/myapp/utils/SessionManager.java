package com.example.myapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "RevisellySession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save user session
    public void createLoginSession(String token, String refreshToken, int userId, String userName, String userEmail, String userRole) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_ROLE, userRole);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Get refresh token
    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    // Get user ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    // Get user email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    // Get user role
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, null);
    }

    // Logout user
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Update token
    public void updateToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }
}

