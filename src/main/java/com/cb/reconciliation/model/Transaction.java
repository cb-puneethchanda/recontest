package com.cb.reconciliation.model;

import org.json.simple.JSONObject;

import java.time.LocalDateTime;

public class Transaction {
    protected String id;
    protected LocalDateTime date;
    protected double amount;
    protected String currencyCode;
    protected String transactionType;

    public Transaction(String id, LocalDateTime date, double amount, String currencyCode) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Transaction(String id, LocalDateTime date, double amount, String currencyCode, String transactionType) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.transactionType = transactionType;
    }

    public Transaction(String id) {
        this.id = id;
        this.amount = 100;
        this.date = LocalDateTime.now();
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("amount", this.getAmount());
        jsonObject.put("currencyCode", this.getCurrencyCode());
        jsonObject.put("date", this.getDate());
        jsonObject.put("transactionType", this.getTransactionType());

        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

}
