package com.example.b00063271.safesplit.Entities;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private List<String> userIds;
    private double billAmt;
    private String id;

    public Bill(){}
    public Bill(List<String> userIds, double billAmt) {
        this.userIds = userIds;
        this.billAmt = billAmt;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public double getBillAmt() {
        return billAmt;
    }

    public void setBillAmt(double billAmt) {
        this.billAmt = billAmt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
