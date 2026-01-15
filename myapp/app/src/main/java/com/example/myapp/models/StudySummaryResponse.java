package com.example.myapp.models;

import java.util.List;

public class StudySummaryResponse {
    private List<StudySessionSummary> summary;

    public List<StudySessionSummary> getSummary() {
        return summary;
    }

    public void setSummary(List<StudySessionSummary> summary) {
        this.summary = summary;
    }
}

