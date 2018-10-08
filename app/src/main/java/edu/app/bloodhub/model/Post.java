package edu.app.bloodhub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    User user;
    String bloodGroup,amount,mobile,address,requiredWithin;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequiredWithin() {
        return requiredWithin;
    }

    public void setRequiredWithin(String requiredWithin) {
        this.requiredWithin = requiredWithin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.bloodGroup);
        dest.writeString(this.amount);
        dest.writeString(this.mobile);
        dest.writeString(this.address);
        dest.writeString(this.requiredWithin);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.bloodGroup = in.readString();
        this.amount = in.readString();
        this.mobile = in.readString();
        this.address = in.readString();
        this.requiredWithin = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
