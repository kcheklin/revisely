package com.example.myapp.models;

import java.util.List;

public class QuotesListResponse {
    private List<Quote> quotes;

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}

