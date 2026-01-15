package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class ChapterResponse {
    @SerializedName("chapter")
    private Chapter chapter;

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}

