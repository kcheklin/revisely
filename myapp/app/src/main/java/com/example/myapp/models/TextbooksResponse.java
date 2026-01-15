package com.example.myapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TextbooksResponse {
    @SerializedName("textbooks")
    private List<Textbook> textbooks;

    public List<Textbook> getTextbooks() {
        return textbooks;
    }

    public void setTextbooks(List<Textbook> textbooks) {
        this.textbooks = textbooks;
    }
}

