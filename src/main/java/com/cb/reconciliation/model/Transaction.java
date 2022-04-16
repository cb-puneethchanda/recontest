package com.cb.reconciliation.model;

import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    protected String customer_name;
    protected String id;
    protected LocalDateTime date;
    protected double amount;
    protected String currencyCode;
    protected String transactionType;
    protected String issues;
    protected String paymentMethod;
    protected String gateWay;
    protected double fee;
    protected double actualamount;

    public double getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Transaction(String idAtGateway, LocalDateTime date, double amount, String currencyCode, String paymentMethod, String gateWay, double fee) {
        this.id = idAtGateway;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.paymentMethod = paymentMethod;
        this.gateWay = gateWay;
        this.fee = fee;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public Transaction(String id, LocalDateTime date, double amount, String currencyCode, String paymentMethod, String gateWay) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.paymentMethod = paymentMethod;
        this.gateWay = gateWay;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }


    public Transaction(String id, String customer_name,LocalDateTime date, double amount, String currencyCode, String transactionType, String paymentMethod, String gateWay) {
        this.id = id;
        this.customer_name=customer_name;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.transactionType = transactionType;
        this.paymentMethod = paymentMethod;
        this.gateWay = gateWay;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", issues='" + issues + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", gateWay='" + gateWay + '\'' +
                ", Fee='" + fee + '\'' +
                ", ActualAmount='" + (amount-fee) + '\'' +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("name",this.getCustomer_name());
        jsonObject.put("amount", this.getAmount()/100.0);
        jsonObject.put("currencyCode", this.getCurrencyCode());
        jsonObject.put("date", this.getDate());
        jsonObject.put("transactionType", this.getTransactionType());
        jsonObject.put("issues", this.getIssues());
        jsonObject.put("paymentMethod", this.getPaymentMethod());
        jsonObject.put("gateWay", this.getGateWay());
        jsonObject.put("gatewayFee", this.getFee()/100.0);
        jsonObject.put("actualamount", this.getAmount()/100.0-this.getFee()/100.0);
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id!=null && that.id!=null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.length();
    }
}
