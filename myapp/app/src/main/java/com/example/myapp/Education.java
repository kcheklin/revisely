package com.example.myapp;

public class Education {

    private String education;
    private String institution;
    private int graduationYear;

    public Education(String education, String institution, int graduationYear) {
        this.education = education;
        this.institution = institution;
        this.graduationYear = graduationYear;
    }

    public String getEducation() {
        return education;
    }

    public String getInstitution() {
        return institution;
    }

    public int getGraduationYear() {
        return graduationYear;
    }
}
