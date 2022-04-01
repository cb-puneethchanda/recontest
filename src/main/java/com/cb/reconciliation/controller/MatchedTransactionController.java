package com.cb.reconciliation.controller;

import com.cb.reconciliation.model.AccSoftEnum;
import com.cb.reconciliation.model.DateModel;
import com.cb.reconciliation.model.GatewayEnum;
import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.credentials.*;
import com.cb.reconciliation.service.ChargebeeConnect;
import com.cb.reconciliation.service.ConvertToJSON;
import com.cb.reconciliation.service.MismatchedTransactions;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/reconciliation")
public class MatchedTransactionController {

    String xeroTenantId = "e39e9d62-44ae-4814-adbc-3b897c9b67bd";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg3OTc2NjIsImV4cCI6MTY0ODc5OTQ2MiwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODc5NzY1MiwieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjljNmM1YmI2MDhmOTRiZGJhODNmMDNkMWRiNzkzN2FkIiwianRpIjoiYmEyYWNjYWQyMjAxODE5OTMxMWVkZjkxMDBjZTdjOWUiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6ImE2ODhkZjA0LThmYmMtNGY0ZC05YmRmLWU2OGM5MjIyODU5MyIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInNzbyJdfQ.LxIEE_4c940eCkmbpnXHvmCyJo6SutaCGeryrv6jyDBR5MiGbenJNy1akZT5o0AXq8YvFg-w0qrgtJ1JUozT5QI4Uia5gdjOoiTSj__3qCL78xT6PxIgHiY8V_RUlpF2oGl_eb7uFRHwCi9ksbc9tFqTDUuAg3udGoikamhiRc4SuLyovAyUKRRESsaBOOIk36EcKTLpxanND7t6OLeOFhj_GFs-B1lzluOt5IN7rAW46IqPszcfsEcjV6-4h1WoVk3P9Xk4DIoMqFbmWjjc-kwY53AOkYPi8RZbA8atcGaHlZPHSKW6PK6new6bi9mhqvBfw_lo9rQ-UxWCvkpi9Q";

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

//    @GetMapping("/mismatched")
//    public JSONObject getMismatched(@RequestParam("start") String start, @RequestParam("end") String end) throws Exception {
//        Timestamp startTime = new Timestamp(Long.parseLong(start) *1000);
//        Timestamp endTime = new Timestamp(Long.parseLong(end) * 1000);
//
//        System.out.println(startTime);
//        System.out.println(endTime);
//
//        MismatchedTransactions computer = new MismatchedTransactions();
//
//        Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
//        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);
//
//        Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();
//        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);
//
//        List<Transaction> finalList = computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startTime, endTime);
////        ChargebeeConnect conn = new ChargebeeConnect();
////        List<Transaction> finalList = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTime, endTime);
//        return ConvertToJSON.transactions(finalList);
//    }


    @PostMapping("/mismatched")
    public String getMismatched(@RequestBody DateModel date) throws Exception {
        Timestamp startTime = date.getStartDate();
        Timestamp endTime = date.getEndDate();

        System.out.println(startTime);
        System.out.println(endTime);

        MismatchedTransactions computer = new MismatchedTransactions();

        Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);

        Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);

        CompletableFuture finalList = computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startTime, endTime);
//        ChargebeeConnect conn = new ChargebeeConnect();
//        List<Transaction> finalList = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTime, endTime);
        return "Done!"+(Thread.currentThread().getName());
    }
}
