package com.example.b00063271.safesplit.Entities;

import java.util.Date;

public class Activities {
    private String activityString;
    private double activityType;
    private Date timeStamp;

    public Date getTimeStamp() { return timeStamp; }

    public void setTimeStamp(Date timeStamp) { this.timeStamp = timeStamp; }

    public Activities() {
    }

    public Activities(String activityString, double activityType, Date timeStamp) {

        this.activityString = activityString;
        this.activityType = activityType;
        this.timeStamp = timeStamp;
    }

    public String getActivityString() {
        return activityString;
    }

    public void setActivityString(String activityString) {
        this.activityString = activityString;
    }

    public double getActivityType() {
        return activityType;
    }

    public void setActivityType(double activityType) {
        this.activityType = activityType;
    }
}
