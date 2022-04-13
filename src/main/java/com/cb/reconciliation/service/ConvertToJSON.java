package com.cb.reconciliation.service;

import com.cb.reconciliation.model.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ConvertToJSON {
    public static JSONObject transactions(List<Transaction> transactionList,List<Transaction> chargebeeTransactions) throws JSONException {
        JSONObject response = new JSONObject();
        JSONArray mismatchedList = new JSONArray();

        int mismatchedCount = 0;

        for (Transaction transaction: transactionList) {
            mismatchedList.put(transaction.toJSONObject());
            mismatchedCount++;
        }

        response.put("mismatched", mismatchedList);

        JSONObject metadata = new JSONObject();
        metadata.put("mismatchedCount", mismatchedCount);

        metadata.put("matchedCount",chargebeeTransactions.size()-mismatchedCount);

        response.put("metadata", metadata);

        return response;
    }
}