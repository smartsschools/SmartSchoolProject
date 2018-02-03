package com.example.smartsolutions.signuputil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mmenem on 01/02/18.
 */

public class TeacherInformation {
    private String name, username, phone, email, downloadUri, userChoiceText;
    private ArrayList<String> gradesList, subjectsList;

    public TeacherInformation() {

    }


    public TeacherInformation(String name, String username, String phone, String email, ArrayList<String> gradesList, ArrayList<String> subjectsList, String userChoiceText) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.gradesList = gradesList;
        this.subjectsList = subjectsList;
        this.userChoiceText = userChoiceText;
    }

    public TeacherInformation(String name, String username, String phone, String email, ArrayList<String> gradesList, ArrayList<String> subjectsList, String downloadUri, String userChoiceText) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.gradesList = gradesList;
        this.subjectsList = subjectsList;
        this.downloadUri = downloadUri;
        this.userChoiceText = userChoiceText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public ArrayList<String> getGradesList() {
        return gradesList;
    }

    public void setGradesList(ArrayList<String> gradesList) {
        this.gradesList = gradesList;
    }

    public ArrayList<String> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(ArrayList<String> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public String getUserChoiceText() {
        return userChoiceText;
    }

    public void setUserChoiceText(String userChoiceText) {
        this.userChoiceText = userChoiceText;
    }
}
