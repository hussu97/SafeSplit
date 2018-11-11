package com.example.b00063271.safesplit.Entities;

public class Transactions {
    private double Amount;
    private String Id;
    private String from;
    private String to;

    public Transactions() {
    }

    public Transactions(double amount, String from, String to) {

        Amount = amount;
        this.from = from;
        this.to = to;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
