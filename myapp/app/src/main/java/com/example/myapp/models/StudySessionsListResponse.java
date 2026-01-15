package com.example.myapp.models;

import java.util.List;

public class StudySessionsListResponse {
    private List<StudySession> sessions;

    public List<StudySession> getSessions() {
        return sessions;
    }

    public void setSessions(List<StudySession> sessions) {
        this.sessions = sessions;
    }
}

