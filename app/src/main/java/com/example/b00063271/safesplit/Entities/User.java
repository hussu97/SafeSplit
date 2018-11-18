package com.example.b00063271.safesplit.Entities;

import java.util.List;

public class User {
    private String ID;
    private String name;
    private String email;
    private String mobile;
    private List<String> transactionIds;
    private List<String> groupIds;

    public User() {
    }

    public User(String name, String email, String mobile, List<String> transactionIds, List<String> groupIds) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.transactionIds = transactionIds;
        this.groupIds = groupIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(List<String> transactionIds) {
        this.transactionIds = transactionIds;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
