package com.cb.reconciliation.service;

import com.cb.reconciliation.model.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public class ConvertToJSONService {
    public static JSONObject transactions(List<Transaction> transactionList) {
        JSONObject response = new JSONObject();
        JSONArray mismatchedList = new JSONArray();

        for (Transaction transaction: transactionList) {
            mismatchedList.add(transaction.toJSONObject());
        }

        response.put("mismatched", mismatchedList);

        JSONObject metadata = new JSONObject();
        response.put("metadata", metadata);

        return response;
    }
}
