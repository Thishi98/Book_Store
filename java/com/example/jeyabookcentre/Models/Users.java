package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class Users implements Serializable{

    String id, dp, fName,lName,email,phone,password,confPass;

    public Users() {

    }

    public Users(String id, String dp, String fName, String lName, String email, String phone, String password, String confPass) {
        this.id = id;
        this.dp = dp;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confPass = confPass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
