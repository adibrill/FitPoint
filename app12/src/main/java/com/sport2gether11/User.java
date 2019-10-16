package com.sport2gether11;

import android.icu.text.Transliterator;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class User {

    private String userName;
    private int timePeriod;
    private String phoneNumber;
    private String password;
    private String email;

    private String sport1;
    private String sport2;
    private String sport3;

    private int level;
    private int userAge;

    private String position;

    public User(String userName, String phoneNumber, String email,int timePeriod, String password,
                int level, int userAge, String position, String sport1, String sport2, String sport3) {
        this.userName = userName;
        this.timePeriod = timePeriod;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.sport1 = sport1;
        this.sport2 = sport2;
        this.sport3 = sport3;
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

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSport1() {
        return sport1;
    }

    public void setSport1(String sport1) {
        this.sport1 = sport1;
    }

    public String getSport2() {
        return sport2;
    }

    public void setSport2(String sport2) {
        this.sport2 = sport2;
    }

    public String getSport3() {
        return sport3;
    }

    public void setSport3(String sport3) {
        this.sport3 = sport3;
    }
}
