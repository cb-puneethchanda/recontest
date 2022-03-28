package com.cb.reconciliation.model;

import org.json.simple.JSONObject;

import java.time.LocalDate;

public class Transaction {
    protected String id;
    protected LocalDate date;
    protected double amount;

    protected String currencyCode;

    public Transaction(String id, LocalDate date, double amount, String currencyCode) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction(String id) {
        this.id = id;
        this.amount = 100;
        this.date = LocalDate.now();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("amount", this.getAmount());
        jsonObject.put("currencyCode", this.getCurrencyCode());
        jsonObject.put("date", this.getDate());

        return jsonObject;
    }
}
