package com.example.b00063271.safesplit.Entities;

import java.util.List;

public class User {
    private String ID;
    private String name;
    private double mobile;
    private List<String> billIds;

    public User(){}
    public User(String name, double mobile, List<String> billIds) {
        this.name = name;
        this.mobile = mobile;
        this.billIds = billIds;
    }

    public List<String> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<String> billIds) {
        this.billIds = billIds;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMobile() {
        return mobile;
    }

    public void setMobile(double mobile) {
        this.mobile = mobile;
    }
}
