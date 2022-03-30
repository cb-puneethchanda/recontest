package com.cb.reconciliation;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.*;
import com.cb.reconciliation.service.ChargebeeConnect;
import com.cb.reconciliation.service.MismatchedTransactions;
import com.cb.reconciliation.service.StripeConnect;
import com.cb.reconciliation.service.XeroConnect;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ReconciliationApplicationTests {
    String xeroTenantId = "e39e9d62-44ae-4814-adbc-3b897c9b67bd";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg2NTc2NzYsImV4cCI6MTY0ODY1OTQ3NiwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODY1NzYzNywieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjU5ZDJiYjA4ODlmNzRjMDlhOGVmMzAzYjk2MmZkNTg2IiwianRpIjoiYmVjNjIxY2QyMDQ0YmE5NTQwYzQ5NGZjZWM1MTc5MzIiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6Ijk4NTQ1ZmJhLWMyMmQtNGViYS1iNDMzLTRmOGVjNjc0ZjI4NyIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInNzbyJdfQ.ZmvmntQXhjoI0zYAb1fO9tt2kQF1z1yRCpGU5ZAWAZAhLFicq1MN3fWWUOzjOj_cHR1zrTMD8LhadkOfupPUTjhSaSk3ZbZkQeZ4PtLBREvAXLdEMwwqp7gFVT88VAoOwgKdarj01Iyav4V_dGJo2ytLG_7UUpDMhv92UvIKbygm9XK7JRXqvu5xDyIoSJwKLz9o6tuFykXADzdyS2xn_uKUYtoJk7uhWLqUmpsqqw_Q2JrFsztpumtoKuZt3nF3DwSW7zbqAQMTVgfDD4Rx9nbQB0Qc_FzQYwInbRMmZ7RObnEqlqKYL64jRc97qHGAsltP0Fs1zyw_hNkuvC6-Aw";
    // XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);
    XeroCredentials xeroCredentials = new XeroCredentials(xeroTenantId, accessToken);

    String chargebeeSiteUrl = "reconciletest-test";
    String chargebeeApiKey = "test_rBsnVbkoMt0ecuqSQlRfH1xYqe3qXBqrJ";
    ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials(chargebeeSiteUrl, chargebeeApiKey);

    String stripeApiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";
    StripeCredentials stripeCredentials = new StripeCredentials(stripeApiKey);

    LocalDateTime startDate = LocalDateTime.of(2022, 3, 22, 0, 0);
    LocalDateTime endDate = LocalDateTime.now();

    @Test void testXero(){
        XeroConnect conn = new XeroConnect();
        List<Transaction> transactions = conn.getTranscations(xeroCredentials, startDate, endDate);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void testChargebee() throws Exception {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        ChargebeeConnect conn = new ChargebeeConnect();
        List<Transaction> transactions;
//        com.chargebee.models.Transaction.Type transactionType = com.chargebee.models.Transaction.Type.REFUND;

        transactions = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

//    @Test
    void testStripe() throws StripeException {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getTransactions(stripeCredentials, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

//    @Test
    void testStripeRefund() throws StripeException {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getRefunds(stripeCredentials, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void testStripeBalanceTransaction() throws StripeException {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getBalanceTransaction(stripeCredentials, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void mismatched() throws Exception {
        MismatchedTransactions computer = new MismatchedTransactions();

        Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);

        Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);

        computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startDate, endDate);
    }
}
