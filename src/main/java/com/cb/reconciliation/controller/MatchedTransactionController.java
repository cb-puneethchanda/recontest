package com.cb.reconciliation.controller;

import com.cb.reconciliation.model.AccSoftEnum;
import com.cb.reconciliation.model.DateModel;
import com.cb.reconciliation.model.GatewayEnum;
import com.cb.reconciliation.model.credentials.*;
import com.cb.reconciliation.service.MismatchedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/reconciliation")
public class MatchedTransactionController {

    String xeroTenantId = "e39e9d62-44ae-4814-adbc-3b897c9b67bd";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg4MDYyMjgsImV4cCI6MTY0ODgwODAyOCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODgwNjIyMCwieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjA1ZTY5NDgyOWI5ZTQ0OGM4MzZjNzg5MGJkNDIyZDJlIiwianRpIjoiYjIxYmZjZTI4OWM3YTkxZTA2MzZiNDAwZTc0N2Y2ZTUiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjlhNTliMzQ3LThhNWQtNDNiOS05YzZlLTJjOTBhZjY1M2Y0ZiIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInNzbyJdfQ.n1QyObyAhOCyKJ_a-pQu-BIWc5B-W4Tm4_jSh0BwJkDD-hyFAQGMUO5zL4A6D_rXVn4-UIaBrqNIwKMxQlg__4vvVDhCYSGCxdZFjaHYuIkmot7bwIf-MRogbgYZCfiVaknzWbCF4iuGtBP5tWtYlqTryywYOLvQs4A-9FhZxn5yBCgFjRG2bJIIm2kZ-cCWuVFBj9dk3n8b0lnCfjyPnNPQa_SKqd7p5TmAIuYHgO9pInscLiZjkZ1XetYJdco8JeWqB16tJ1NPUrtcpORs6e8WbzFFFyfYxW-Uq_fdCwGM-aOpgEDh0Kr-IKXgJ9O9tbKcPIE50IJ5VR-u9rfsmw";

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
    @Autowired
    private MismatchedTransactionsService computer;

    @PostMapping("/mismatched")
    public String getMismatched(@RequestBody DateModel date) throws Exception {
        Timestamp startTime = date.getStartDate();
        Timestamp endTime = date.getEndDate();

        System.out.println(startTime);
        System.out.println(endTime);

//        MismatchedTransactionsService computer = new MismatchedTransactionsService();

        Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);

        Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);
        String jobId = "CB-RECONCILE-JOB-" + UUID.randomUUID().toString();
        //todo: create a job id and assign status as syncing. and return the job id
        //todo: create a get request api to check the status - refresh button call
        //todo: create a get request to get the report once the status is done

        computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startTime, endTime);
//        ChargebeeConnect conn = new ChargebeeConnect();
//        List<Transaction> finalList = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTime, endTime);
        return jobId;
    }
}
