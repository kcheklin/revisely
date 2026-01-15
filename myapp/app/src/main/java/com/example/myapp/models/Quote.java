package com.example.myapp.models;

public class Quote {
    private int id;
    private String text;
    private String author;
    private String category; // "motivation", "success", "learning", "perseverance"
    private int likes;

    // Constructors
    public Quote() {
    }

    public Quote(String text, String author, String category) {
        this.text = text;
        this.author = author;
        this.category = category;
        this.likes = 0;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getLikes() {
        return likes;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Utility method for display
    public String getFormattedQuote() {
        if (author != null && !author.isEmpty()) {
            return "\"" + text + "\" - " + author;
        } else {
            return "\"" + text + "\"";
        }
    }
}

