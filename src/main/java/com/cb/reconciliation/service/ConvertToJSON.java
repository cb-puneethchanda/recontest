package com.cb.reconciliation.service;

import com.cb.reconciliation.model.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ConvertToJSON {
    public static JSONObject transactions(List<Transaction> transactionList) throws JSONException {
        JSONObject response = new JSONObject();
        JSONArray mismatchedList = new JSONArray();

        for (Transaction transaction: transactionList) {
            mismatchedList.put(transaction.toJSONObject());
        }

        response.put("mismatched", mismatchedList);

        JSONObject metadata = new JSONObject();
        response.put("metadata", metadata);

        return response;
    }
}