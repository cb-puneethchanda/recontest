package com.cb.reconciliation.utils;

import com.cb.reconciliation.model.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            if (reference == null || reference.equals("null")) {
//                System.out.println("XERO null id: " + payment.get(i) + i + " " + payments.toArray().length);
                continue;
            }
            String id = reference.split("[|]")[0].split("[:]")[1].trim();
            // Amount
            double amount = (double) payment.get("Amount");
            // Currency
            JSONObject invoice = (JSONObject) payment.get("Invoice");
            String currencyCode = (String) invoice.get("CurrencyCode");
            // Date
            String dateFromJSON = (String) payment.get("Date");
            String epochString = dateFromJSON.split("[(]")[1].split("[+]")[0].trim();
            LocalDateTime date = Instant.ofEpochMilli(Long.parseLong(epochString)).atZone(ZoneId.systemDefault()).toLocalDateTime();

            Transaction transaction = new Transaction(id, date, amount, currencyCode);
            transactions.add(transaction);
        }
        return transactions;
    }
}


