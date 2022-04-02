package com.cb.reconciliation.controller;

import com.cb.reconciliation.model.AccSoftEnum;
import com.cb.reconciliation.model.GatewayEnum;
import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.credentials.*;
import com.cb.reconciliation.service.ConvertToJSON;
import com.cb.reconciliation.service.MismatchedTransactions;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/reconciliation")
public class MatchedTransactionController {

    String xeroTenantId = "e39e9d62-44ae-4814-adbc-3b897c9b67bd";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg3MDcwNjQsImV4cCI6MTY0ODcwODg2NCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODcwNjkzNywieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjFmOTM3MWU0MTQ4ZDRlZGY4Y2JhZDZiZGExMmRlMTlhIiwianRpIjoiYWI5MjdjYThjOGRiMTA1Yjg5ZDNmZDg3ZjNiMTU4YTUiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjY5NWQxMWNmLTgyNzUtNGE3Zi05OGZiLTJiZDliYjk4M2M4NSIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInB3ZCJdfQ.yHGHnN6mZm5rj1s9KsC4TB7gbUITWMXZEkaIEh5oplUsu7f7sMjV3pS-mgIa0SsFvPE9h3PFJYYXMUGVx_bnK5BZbVFGM-luEdsgPBcHy8T_gPtu7YnvKeoLbWuxXsUHGNB7puq27fyUlbMzaPUBZMiZUSirzX1W_m4dYXB0NFwPFSmF3sZKG8nWaYtIQIjb92xiE-G0sra6ICpnLddigteT-SPOWXzjMS0ZjAD6IdR0zX98NXNR59lwguN7hGP2LqhrlxQiK-hVhdvZGlvefTegaw16LSZF27f6SNdVNdb7HO0fWeTiZCQBLXTq76QJ5qIFrO1jEFAKFC3zvpXARw";
    // XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);
    XeroCredentials xeroCredentials = new XeroCredentials(xeroTenantId, accessToken);

    String chargebeeSiteUrl = "reconciletest-test";
    String chargebeeApiKey = "test_rBsnVbkoMt0ecuqSQlRfH1xYqe3qXBqrJ";
    ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials(chargebeeSiteUrl, chargebeeApiKey);

    String stripeApiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";
    StripeCredentials stripeCredentials = new StripeCredentials(stripeApiKey);

//    LocalDateTime startDate = LocalDateTime.of(2022, 3, 22, 0, 0);
//    LocalDateTime endDate = LocalDateTime.now();

//    @GetMapping("/test")
//    public void demo(@RequestParam("start") String startTime, @RequestParam("end") String endTime) throws Exception {
//        LocalDateTime startDate = LocalDateTime.ofEpochSecond(Long.parseLong(startTime), 0, ZoneOffset.UTC);
//        LocalDateTime endDate = LocalDateTime.ofEpochSecond(Long.parseLong(endTime), 0, ZoneOffset.UTC);
//
//        System.out.println(startDate);
//        System.out.println(endDate);
//    }

    @GetMapping("/mismatched")
    public JSONObject getMismatched(@RequestParam("start") String start, @RequestParam("end") String end) throws Exception {
        Timestamp startTime = new Timestamp(Long.parseLong(start) *1000);
        Timestamp endTime = new Timestamp(Long.parseLong(end) * 1000);

        System.out.println(startTime);
        System.out.println(endTime);

        MismatchedTransactions computer = new MismatchedTransactions();

        Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);

        Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);

        List<Transaction> finalList = computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startTime, endTime);
//        ChargebeeConnect conn = new ChargebeeConnect();
//        List<Transaction> finalList = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTime, endTime);
        return ConvertToJSON.transactions(finalList);
    }

}
