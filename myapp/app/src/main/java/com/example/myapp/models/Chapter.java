package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Chapter {
    @SerializedName("id")
    private int id;

    @SerializedName("textbookId")
    private int textbookId;

    @SerializedName("chapterNumber")
    private int chapterNumber;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("topics")
    private List<String> topics;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("Textbook")
    private Textbook textbook;

    @SerializedName("Quizzes")
    private List<Quiz> quizzes;

    // Constructors
    public Chapter() {}

    public Chapter(int id, String title, int chapterNumber) {
        this.id = id;
        this.title = title;
        this.chapterNumber = chapterNumber;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(int textbookId) {
        this.textbookId = textbookId;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Textbook getTextbook() {
        return textbook;
    }

    public void setTextbook(Textbook textbook) {
        this.textbook = textbook;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}

