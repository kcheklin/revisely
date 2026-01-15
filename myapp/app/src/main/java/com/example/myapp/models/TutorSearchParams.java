package com.example.myapp.models;

/**
 * Helper class for tutor search query parameters
 * Matches the backend API filters in /backend/routes/tutors.js
 */
public class TutorSearchParams {
    private String name;
    private String subject;
    private String date; // Format: YYYY-MM-DD
    private String rating; // Minimum rating (e.g., "4.0")

    public TutorSearchParams() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * Build query string for API call
     * @return Query parameters as a formatted string
     */
    public String toQueryString() {
        StringBuilder query = new StringBuilder();
        
        if (name != null && !name.isEmpty()) {
            query.append("name=").append(name).append("&");
        }
        if (subject != null && !subject.isEmpty()) {
            query.append("subject=").append(subject).append("&");
        }
        if (date != null && !date.isEmpty()) {
            query.append("date=").append(date).append("&");
        }
        if (rating != null && !rating.isEmpty()) {
            query.append("rating=").append(rating).append("&");
        }
        
        String result = query.toString();
        return result.isEmpty() ? "" : result.substring(0, result.length() - 1);
    }
}

