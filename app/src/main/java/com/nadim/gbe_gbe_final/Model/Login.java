package com.nadim.gbe_gbe_final.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("otpNumber")
    @Expose
    private Object otpNumber;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getOtpNumber() {
        return otpNumber;
    }

    public void setOtpNumber(Object otpNumber) {
        this.otpNumber = otpNumber;
    }
}
