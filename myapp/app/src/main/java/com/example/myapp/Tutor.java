package com.example.myapp;

import java.util.List;

public class Tutor {

<<<<<<< HEAD
    @com.google.gson.annotations.SerializedName("id")
=======
>>>>>>> bd114bac6a70ab1f02e6755026cdb2a87cfd4084
    private String tutorId;
    private String name;
    private String faculty;
    private List<String> subjects;
    private int avatarId;
    private String role;
    private String about;
    private int noStudents;
    private int noSessions;
    private int yearsExperience;
    private List<Integer> starRatings;
    private List<String> subjectMinLevel;
    private List<String> subjectMaxLevel;
    private List<String> education;
    private List<String> institutions;
    private List<Integer> graduationYears;
    private List<String> timeSlots;
    private List<Boolean> timeSlotsAvailability;
    private List<String> reviewName;
    private List<Integer> reviewAvatarId;
    private List<String> reviewContent;
    private List<Integer> reviewStar;
    private List<Integer> reviewLikes;

<<<<<<< HEAD
    public Tutor() {}

=======
>>>>>>> bd114bac6a70ab1f02e6755026cdb2a87cfd4084
    public Tutor(String tutorId, String name, String faculty, List<String> subjects, int avatarId, String role, String about, int noStudents, int noSessions, int yearsExperience, List<Integer> starRatings, List<String> subjectMinLevel, List<String> subjectMaxLevel, List<String> education, List<String> institutions, List<Integer> graduationYears, List<String> timeSlots, List<Boolean> timeSlotsAvailability, List<String> reviewName, List<Integer> reviewAvatarId, List<String> reviewContent, List<Integer> reviewStar, List<Integer> reviewLikes) {
        this.tutorId = tutorId;
        this.name = name;
        this.faculty = faculty;
        this.subjects = subjects;
        this.avatarId = avatarId;
        this.role = role;
        this.about = about;
        this.noStudents = noStudents;
        this.noSessions = noSessions;
        this.yearsExperience = yearsExperience;
        this.starRatings = starRatings;
        this.subjectMinLevel = subjectMinLevel;
        this.subjectMaxLevel = subjectMaxLevel;
        this.education = education;
        this.institutions = institutions;
        this.graduationYears = graduationYears;
        this.timeSlots = timeSlots;
        this.timeSlotsAvailability = timeSlotsAvailability;
        this.reviewName = reviewName;
        this.reviewAvatarId = reviewAvatarId;
        this.reviewContent = reviewContent;
        this.reviewStar = reviewStar;
        this.reviewLikes = reviewLikes;
    }

    public String getTutorId() {
        return tutorId;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public double getAverageRating() {
        if(starRatings == null){
            return 0;
        }else{
            double total = 0;
            int count = 0;
            for(int i = 0; i < starRatings.size(); i++){
                int noRatings = starRatings.get(i);
                total += (5 - i) * noRatings;
                count += noRatings;
            }
            return count == 0 ? 0 : total / count;
        }
    }

    public int getTotalRatings() {
        if(starRatings == null){
            return 0;
        }else{
            int count = 0;
            for(Integer rating : starRatings){
                count += rating;
            }
            return count;
        }
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public String getRole(){
        return role;
    }

    public String getAbout() {
        return about;
    }

    public int getNoStudents() {
        return noStudents;
    }

    public int getNoSessions() {
        return noSessions;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public List<Integer> getStarRatings() {
        return starRatings;
    }

    public List<String> getSubjectMinLevel() {
        return subjectMinLevel;
    }

    public List<String> getSubjectMaxLevel() {
        return subjectMaxLevel;
    }

    public List<String> getEducation() {
        return education;
    }

    public List<String> getInstitutions() {
        return institutions;
    }

    public List<Integer> getGraduationYears() {
        return graduationYears;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public List<Boolean> getTimeSlotsAvailability() {
        return timeSlotsAvailability;
    }

    public List<String> getReviewName() {
        return reviewName;
    }

    public List<Integer> getReviewAvatarId() {
        return reviewAvatarId;
    }

    public List<String> getReviewContent() {
        return reviewContent;
    }

    public List<Integer> getReviewStar() {
        return reviewStar;
    }

    public List<Integer> getReviewLikes() {
        return reviewLikes;
    }
}
