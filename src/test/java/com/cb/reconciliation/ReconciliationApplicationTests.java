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

    //        String xeroTenantId = "8940aed6-420b-4a89-be09-a9c084f05702";
//        String refreshToken = "d6e245efca29fce67af4e8a31024bb772b0295a2c26b5e7574e378bcb88c7617";
//        String clientId = "0A33E85DDDB74CA5A5D08A2A178A844A";
//        String clientSecret = "H6e8vmVWfg73WeI7GQ1ZJupi0Eo4BeNW_TQxA6oMGEzpFUhl";

//        String xeroTenantId = "9b3db3d6-ef3e-4335-b9ff-4edf5d34b19c";
//        String refreshToken = "2df0720cc487836ae71df3359681791f4b1d8482aa236230a61aafef3d9253c6";
//        String clientId = "28B2CA27997343BAB89842D94CBFEF42";
//        String clientSecret = "iiQjJ0De4pliVffxoRuJL5W8MeezAjyw_ziiFKxVVExsULk1";

    String xeroTenantId = "11fc848b-ac5c-43eb-8c35-39043cad93e7";
    String refreshToken = "2df0720cc487836ae71df3359681791f4b1d8482aa236230a61aafef3d9253c6";
    String clientId = "58AA430FE9794CF082C7D3632850BD46";
    String clientSecret = "Op_Gd67qJMMXDkwUZ0lV58gFTB-klR7kEnbIDkTmSQVP7a6l";
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg2Mzg5MjYsImV4cCI6MTY0ODY0MDcyNiwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiNThBQTQzMEZFOTc5NENGMDgyQzdEMzYzMjg1MEJENDYiLCJzdWIiOiI5MjVmYmEyZGNjZjI1MWEzOTEyMWUyZTg4M2I5OTk2NCIsImF1dGhfdGltZSI6MTY0ODYzODkwOSwieGVyb191c2VyaWQiOiI4ZTZhZDI1NS1jMDlkLTQzYmEtODY1NS0wYTcyYjY2MDRhOTciLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjhmNjc1OTlmYWUxYzQ5ZDFiNjJmOTE1ZGE4NTVhNGZlIiwianRpIjoiMzUzMjFlNTg1OGYyNzg1MjQ1YTAzZGRiZDQ0Nzg5ZjEiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjhiZDNiOTdhLTU2ZGMtNDgxMy1hOTg2LWRjMmQyMDc2OTg0ZSIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsImFjY291bnRpbmcuY29udGFjdHMiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.iR1EgaIrzO0W_jG2mbf6pURPx2PygUAerJ4SpSTbsDWRBd6j9ZhHf5MacvlhLEOVgYZCD-R7w-HiHJYIixV4uEdWqjaFT4lsTztYqAwDYgzvYbonDLGr_2kRgbDh7EV2_xJ4QBAV2YKcMNXA40M3f-vVKpMEcjtkGZU2Mh2cxeTlBwZKhY_3J_pVj_-1rWjQ97XEOsmnG8qC--B3Kh3g-GJaju3wRK-sXlCra0IEYei7xe2ujaZBUsUJNPvemWLJKDdhqQrvzpfzTH-zJKn-QptXUEz67wQJqmdgZUUAVpRernbk4X2WIqnDE4GTNU0nUAygwm6rSvJdMOldpOnCBw";
    // XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);
    XeroCredentials xeroCredentials = new XeroCredentials(xeroTenantId, accessToken);

    String chargebeeSiteUrl = "xaio-yan-test";
    String chargebeeApiKey = "test_aPW2LccuW8AfjCUxnUl5ZXBlC7dG4UeXG";
    ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials(chargebeeSiteUrl, chargebeeApiKey);
    //        ChargebeeCredentials credentials = new ChargebeeCredentials("puneethintern-test",
//                "test_PaDmUSQGN1Z0dRCkpdBZ1DLQPMf7jvZw");

    String stripeApiKey = "sk_test_51Kgn4aSHg3lW4nFeGQTCtYFmp1ZGTUWIBSyuoTq0rQIxmfBScW1HLcUnnDw8I1Mae5Bo2WmLT89aHNeASzqa4OsL00Ww9J1pKt";
    StripeCredentials stripeCredentials = new StripeCredentials(stripeApiKey);

    LocalDateTime startDate = LocalDateTime.of(2022, 3, 22, 0, 0);
    LocalDateTime endDate = LocalDateTime.now();

    @Test void testXero(){
        XeroConnect conn = new XeroConnect();
        LocalDate startDate = LocalDate.of(2020, 3, 22);LocalDate endDate = LocalDate.now();

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
        com.chargebee.models.Transaction.Type transactionType = com.chargebee.models.Transaction.Type.REFUND;
//        com.chargebee.models.Transaction.Type transactionType = com.chargebee.models.Transaction.Type.PAYMENT;

        transactions = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
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

    @Test
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

        computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startDate.toLocalDate(), endDate.toLocalDate());
    }
}
