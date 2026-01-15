package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Textbook {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("subject")
    private String subject;

    @SerializedName("grade")
    private String grade;

    @SerializedName("level")
    private String level;

    @SerializedName("description")
    private String description;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("yearPublished")
    private Integer yearPublished;

    @SerializedName("language")
    private String language;

    @SerializedName("Chapters")
    private List<Chapter> chapters;

    // Constructors
    public Textbook() {}

    public Textbook(int id, String title, String subject, String grade, String description) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.grade = grade;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}

