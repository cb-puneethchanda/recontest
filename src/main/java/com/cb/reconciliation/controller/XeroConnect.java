package com.cb.reconciliation.controller;

import com.cb.reconciliation.model.XeroTransaction;
import com.cb.reconciliation.utils.ParseTransaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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

    String access_token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg0NjY3MDUsImV4cCI6MTY0ODQ2ODUwNSwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMEEzM0U4NUREREI3NENBNUE1RDA4QTJBMTc4QTg0NEEiLCJzdWIiOiIxODM1Mjg5OWZjZDU1OTU1Yjk0ZmIwNmNiMzQ0Y2Y2NSIsImF1dGhfdGltZSI6MTY0ODQ0OTc3MCwieGVyb191c2VyaWQiOiJjMWIwNGI1ZC1kNWJjLTRhYzItODMxMy00OTgyMjNmZGM3ZDEiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjhmNDhiZmU0OWI0NDQ1NDU5ZjlkZWI0OWJhYjFmODAxIiwianRpIjoiNGI4OWNmMDQ5M2Q3NjAwYWVhOGEzNTcxNjgyZWVlZjMiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjhmZWNjNTZiLWQ1MDAtNDBjZS1hM2QwLTEzOTJmODZjNWMxMSIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsImFjY291bnRpbmcuY29udGFjdHMiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.YfsTunE22-TqrQmMg-a23GO8uWejV-yIt9cNU5kH94_ibcr-AzQL0Lb-m3uodW_zLY3P-7LCrmC3ZOc6a7qbxXKXdNLGjnhfn6UmC4a8_ZBZ_EpgSHiIqzA-jiOo_zlQyDJHlM3jdQ-zAyGz_1AxN8uNafaNzF9gr4HllaYUFgJkRfmwZDIpNqp0q4nX7-zsA8Wsrpb9aUh-1aBY9apZeOcivvz4R_7ffcv4ObzAqVmSWPr-fagizboTRWi9Pn7QjEJfqCAOYgODl_cNbOhxlEXNO5u7vkwA5fJIIl_LB08AQEjkdf5UUaHOg0ct4WQHuYB7q2gGy-G_VXRCjMhMcQ";

    @GetMapping()
    public JSONObject payments() {
        String jsonResponse = restTemplate.getForObject("http://localhost:8080/xero/connect/raw", String.class);

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

    @GetMapping("/raw")
    public JSONObject xero_api_call() {
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

        JSONObject response = (JSONObject) JSONValue.parse(jsonResponse);
        return response;
    }


}
