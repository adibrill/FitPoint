package com.sport2gether11;

import android.icu.text.Transliterator;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class User {

    private String userName;
    private String timePeriod;
    private String phoneNumber;
    private String password;
    private String email;

    private List<String> sports;

    private int level;
    private int userAge;

    private String position;

    public User(String userName, String phoneNumber, String email,String timePeriod, String password , List<String> sports, int level, int userAge, String position) {
        this.userName = userName;
        this.timePeriod = timePeriod;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.sports = sports;
        this.level = level;
        this.userAge = userAge;
        this.position = position;
    }

    public User(String username, String phoneNumber, String emailInput) {
        this.userName = username;
        this.phoneNumber = phoneNumber;
        this.email = emailInput;

    }

    public User(String username, String phoneNumber, String emailInput, String pos) {
        this.userName = username;
        this.phoneNumber = phoneNumber;
        this.email = emailInput;
        this.position = pos;

    }

    public User() {
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
}
