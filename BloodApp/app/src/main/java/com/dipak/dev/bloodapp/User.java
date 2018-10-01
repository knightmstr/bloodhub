package com.dipak.dev.bloodapp;

public class User {

    //Data Variables
    private String username;
    private String dob;
    private String address;
    private String blood_group;
    private String donation_Date;
    private String mobile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDonation_Date() {
        return donation_Date;
    }

    public void setDonation_Date(String donation_Date) {
        this.donation_Date = donation_Date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}