package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class PublisherModel implements Serializable {

    String id,email,password,confPass,phone,pubName,pubStatus,dp;

    public PublisherModel() {
    }

    public PublisherModel(String id, String email, String password, String confPass, String phone, String pubName, String pubStatus, String dp) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confPass = confPass;
        this.phone = phone;
        this.pubName = pubName;
        this.pubStatus = pubStatus;
        this.dp = dp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPass() {
        return confPass;
    }

    public void setConfPass(String confPass) {
        this.confPass = confPass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public String getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(String pubStatus) {
        this.pubStatus = pubStatus;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
