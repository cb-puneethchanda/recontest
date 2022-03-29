package com.example.recontest.models;

import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.Objects;

public class XeroTransaction {
    protected String id;
    protected LocalDate date;
    protected double amount;

    protected String currencyCode;

    public XeroTransaction(String id, LocalDate date, double amount, String currencyCode) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public XeroTransaction(String id) {
        this.id = id;
        this.amount = 100;
        this.date = LocalDate.now();
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("amount", this.getAmount());
        jsonObject.put("currencyCode", this.getCurrencyCode());
        jsonObject.put("date", this.getDate());

        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XeroTransaction that = (XeroTransaction) o;
        return Double.compare(that.amount, amount) == 0 && id.equals(that.id) && Objects.equals(date, that.date) && Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, amount, currencyCode);
    }
}
