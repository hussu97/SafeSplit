package com.example.b00063271.safesplit.Entities;

public class NotificationText {
    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public NotificationText(String notificationText, String notificationID) {
        this.notificationText = notificationText;
        this.notificationID = notificationID;
    }

    private String notificationText;
    private String notificationID;
}
