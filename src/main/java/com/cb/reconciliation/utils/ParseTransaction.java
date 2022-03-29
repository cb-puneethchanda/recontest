package com.cb.reconciliation.utils;

import com.cb.reconciliation.model.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ParseTransaction {

    public static List<Transaction> xero(String payment, String version) {
        switch (version) {
            default:
                return xeroDefault(payment);
        }
    }

    public static List<Transaction> xeroDefault(String jsonIn) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonIn);
        JSONArray payments = (JSONArray) jsonObject.get("Payments");

        List<Transaction> transactions = new ArrayList<Transaction>();
        for (int i = 0; i < payments.toArray().length; i++) {
            JSONObject payment = (JSONObject) payments.get(i);

            // Extract PG_ID from Reference
            String reference = (String) payment.get("Reference");
            String id = reference.split("[|]")[0].split("[:]")[1].trim();
            // Amount
            double amount = (double) payment.get("Amount");
            // Currency
            JSONObject invoice = (JSONObject) payment.get("Invoice");
            String currencyCode = (String) invoice.get("CurrencyCode");
            // Date
            String dateFromJSON = (String) payment.get("Date");
            String epochString = dateFromJSON.split("[(]")[1].split("[+]")[0].trim();
            LocalDate date = Instant.ofEpochMilli(Long.parseLong(epochString)).atZone(ZoneId.systemDefault()).toLocalDate();

            Transaction Transaction = new Transaction(id, date, amount, currencyCode);
            transactions.add(Transaction);
        }
        return transactions;
    }
}


