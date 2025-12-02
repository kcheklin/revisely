package com.example.myapp;

public class Session {
    private String subject;
    private String tutor;
    private String date;
    private String time;
    private String location;
    private String meetingLink;
    private SessionStatus status;
    private boolean isPending;
    private boolean waitingForConfirmation;

    public Session(String subject, String tutor, String date, String time,
                   String location, String meetingLink, SessionStatus status) {
        this.subject = subject;
        this.tutor = tutor;
        this.date = date;
        this.time = time;
        this.location = location;
        this.meetingLink = meetingLink;
        this.status = status;
        this.isPending = false;
        this.waitingForConfirmation = false;
    }

    public Session(String subject, String tutor, String date, String time,
                   String location, String meetingLink, SessionStatus status,
                   boolean waitingForConfirmation) {
        this(subject, tutor, date, time, location, meetingLink, status);
        this.waitingForConfirmation = waitingForConfirmation;
    }

    // Getters
    public String getSubject() {
        return subject; }

    public String getTutor() {
        return tutor; }

    public String getDate() {
        return date; }

    public String getTime() {
        return time; }

    public String getLocation() {
        return location; }

    public String getMeetingLink() {
        return meetingLink; }

    public SessionStatus getStatus() {
        return status; }

    public boolean isPending() {
        return isPending; }

    public boolean isWaitingForConfirmation() {
        return waitingForConfirmation; }
}
