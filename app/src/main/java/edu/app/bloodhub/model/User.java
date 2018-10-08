package edu.app.bloodhub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String fullName,userName,password,address,phone,dob,ldo,bloodGroup,sex;

    public String getSex() {
        return sex;
    }

    public String getDob() {
        return dob;
    }

    public String getLdo() {
        return ldo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setLdo(String ldo) {
        this.ldo = ldo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.dob);
        dest.writeString(this.ldo);
        dest.writeString(this.bloodGroup);
        dest.writeString(this.sex);
    }

    protected User(Parcel in) {
        this.fullName = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.dob = in.readString();
        this.ldo = in.readString();
        this.bloodGroup = in.readString();
        this.sex = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
