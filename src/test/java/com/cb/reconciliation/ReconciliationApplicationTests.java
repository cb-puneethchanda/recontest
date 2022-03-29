package com.cb.reconciliation;

import com.cb.reconciliation.model.ChargebeeCredentials;
import com.cb.reconciliation.model.StripeCredentials;
import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.XeroCredentials;
import com.cb.reconciliation.service.ChargebeeConnect;
import com.cb.reconciliation.service.StripeConnect;
import com.cb.reconciliation.service.XeroConnect;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ReconciliationApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    @Test void testXero(){
        String xeroTenantId = "8940aed6-420b-4a89-be09-a9c084f05702";
        String refreshToken = "56e214b1f530cc4e28a30152deb603ce382ffea11709a523a6e24842c62b76af";
        String clientId = "0A33E85DDDB74CA5A5D08A2A178A844A";
        String clientSecret = "H6e8vmVWfg73WeI7GQ1ZJupi0Eo4BeNW_TQxA6oMGEzpFUhl";

        String idAtGateway = "ch_3KgVfHSEMRaUmSTA0wbseA5x";
        String chargebeeTxnID = "txn_AzqYRgT0utm185Db";

        XeroConnect conn = new XeroConnect();
        XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);

        LocalDate startDate = LocalDate.of(2020, 3, 22);LocalDate endDate = LocalDate.now();

        List<Transaction> transactions = conn.getTranscations(cred, startDate, endDate);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void testChargebee() throws Exception {
        ChargebeeCredentials credentials = new ChargebeeCredentials("puneethintern-test",
                "test_PaDmUSQGN1Z0dRCkpdBZ1DLQPMf7jvZw");
        LocalDateTime startDate = LocalDateTime.of(2022, 3, 22, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        ChargebeeConnect conn = new ChargebeeConnect();
        List<Transaction> transactions;

        transactions = conn.getTransactions(credentials, "stripe", startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void testStripe() throws StripeException {
        StripeCredentials credentials = new StripeCredentials("sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc");
        LocalDateTime startDate = LocalDateTime.of(2022, 3, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getTransactions(credentials, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }

    }
}
