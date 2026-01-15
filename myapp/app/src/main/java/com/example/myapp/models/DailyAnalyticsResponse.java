package com.example.myapp.models;

import java.util.List;

public class DailyAnalyticsResponse {
    private String date;
    private List<AnalyticsData> data;

    public String getDate() {
        return date;
    }

    public List<AnalyticsData> getData() {
        return data;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setData(List<AnalyticsData> data) {
        this.data = data;
    }
}

