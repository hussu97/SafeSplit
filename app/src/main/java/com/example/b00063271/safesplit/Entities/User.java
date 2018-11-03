package com.example.b00063271.safesplit.Entities;

import java.util.List;

public class User {
    private String ID;
    private String name;
    private String email;
    private List<String> billIds;

    public User(){}
    public User(String name, String email, List<String> billIds) {
        this.name = name;
        this.email = email;
        this.billIds = billIds;
    }
    public User(String name, String email){
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
