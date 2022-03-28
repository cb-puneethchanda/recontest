package com.cb.reconciliation.controller;

import com.cb.reconciliation.model.XeroTransaction;
import com.cb.reconciliation.utils.ParseTransaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("xero/connect")
public class XeroConnect {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public JSONObject payments() {
        String access_token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg0NjM5MjYsImV4cCI6MTY0ODQ2NTcyNiwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMEEzM0U4NUREREI3NENBNUE1RDA4QTJBMTc4QTg0NEEiLCJzdWIiOiIxODM1Mjg5OWZjZDU1OTU1Yjk0ZmIwNmNiMzQ0Y2Y2NSIsImF1dGhfdGltZSI6MTY0ODQ0OTc3MCwieGVyb191c2VyaWQiOiJjMWIwNGI1ZC1kNWJjLTRhYzItODMxMy00OTgyMjNmZGM3ZDEiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjhmNDhiZmU0OWI0NDQ1NDU5ZjlkZWI0OWJhYjFmODAxIiwianRpIjoiNGI4OWNmMDQ5M2Q3NjAwYWVhOGEzNTcxNjgyZWVlZjMiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjhmZWNjNTZiLWQ1MDAtNDBjZS1hM2QwLTEzOTJmODZjNWMxMSIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsImFjY291bnRpbmcuY29udGFjdHMiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.tIc22Ba2J5So_RgpbOzXF0anYc3s3mNBpVg6vjUspPKeMNsnTEhGnPYPoTpEuYqgXQuCQwRsv8KbNubZO0fT2qPwprOt1MZnKdsbPE-aNNyp-pnKp9hkYpc6t5SgXrNEyagS5CbsJokbyMAvuOEweUWESF0XtHOlVRLThorEmelMSY-x4pC-vxW0Z7gVgZN3NWDRZmzCFscvaRscPR6-CJ7N5TuDQG8hdiXesb6J0dadX1QYNxd0TvzDZVatpF93Ali2R7gvw3uUScdNlh_Sf0xV2xZw-xcFpsqlhCLxeVeYuL_kidTchYTZ5hvUoT61veapceDmNpTTN0RMoppQzw";
        String xero_tenant_id = "8940aed6-420b-4a89-be09-a9c084f05702";
        String xeroPaymentAPI = "https://api.xero.com/api.xro/2.0/Payments";

        HttpHeaders headers = new HttpHeaders();
        headers.set("xero-tenant-id", xero_tenant_id);
        headers.set("Authorization", "Bearer " + access_token);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        // API to xero
        HttpEntity<String> entity = new HttpEntity<>(headers);
        System.out.println("GET " + xeroPaymentAPI);
        String jsonResponse = restTemplate.exchange(xeroPaymentAPI, HttpMethod.GET, entity, String.class).getBody();

        // Parse and Extract
        List<XeroTransaction> xeroTransactions = ParseTransaction.xero(jsonResponse, "default");

        // Response JSON
        JSONArray transactionsJSON = new JSONArray();
        for (XeroTransaction transaction : xeroTransactions) {
            transactionsJSON.add(transaction.toJSONObject());
        }

        JSONObject response = new JSONObject();
        response.put("transactions", transactionsJSON);

        return response;
    }
}
