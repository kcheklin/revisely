package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;

public class TextbookResponse {
    @SerializedName("textbook")
    private Textbook textbook;

    public Textbook getTextbook() {
        return textbook;
    }

    public void setTextbook(Textbook textbook) {
        this.textbook = textbook;
    }
}

