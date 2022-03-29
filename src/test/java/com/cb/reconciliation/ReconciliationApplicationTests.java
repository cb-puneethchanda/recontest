package com.cb.reconciliation;

import com.cb.reconciliation.model.ChargebeeCredentials;
import com.cb.reconciliation.model.StripeCredentials;
import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.XeroCredentials;
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
import java.util.List;
import java.util.Map;

@SpringBootTest
class ReconciliationApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    @Test void testXero(){
        String xeroTenantId = "8940aed6-420b-4a89-be09-a9c084f05702";
        String refreshToken = "d6e245efca29fce67af4e8a31024bb772b0295a2c26b5e7574e378bcb88c7617";
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

    @Test
    void testMismatched() throws Exception {
        LocalDate startDate = LocalDate.of(2020, 3, 11);
        LocalDate endDate = LocalDate.now();
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay());

        // xero
        String xeroTenantId = "8940aed6-420b-4a89-be09-a9c084f05702";
        String refreshToken = "d2af37d777234283ca684fe50e83bd344228e14c363ab7d654b9dadbb72d9e93";
        String clientId = "0A33E85DDDB74CA5A5D08A2A178A844A";
        String clientSecret = "H6e8vmVWfg73WeI7GQ1ZJupi0Eo4BeNW_TQxA6oMGEzpFUhl";

        String idAtGateway = "ch_3KgVfHSEMRaUmSTA0wbseA5x";
        String chargebeeTxnID = "txn_AzqYRgT0utm185Db";

        XeroConnect xeroConn = new XeroConnect();
        XeroCredentials xeroCredentials = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);

        List<Transaction> accSoftTransactions = xeroConn.getTranscations(xeroCredentials, startDate, endDate);



        // cb
        ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials("puneethintern-test",
                "test_PaDmUSQGN1Z0dRCkpdBZ1DLQPMf7jvZw");

        ChargebeeConnect ChargebeeConn = new ChargebeeConnect();
        List<Transaction> chargebeeTransactions = ChargebeeConn.getTransactions(chargebeeCredentials, "stripe", startTimestamp, endTimestamp);

        // stripe
        StripeCredentials stripeCredentials = new StripeCredentials("sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc");

        StripeConnect conn = new StripeConnect();
        List<Transaction> gatewayTransactions = conn.getTransactions(stripeCredentials, startTimestamp, endTimestamp);

        System.out.println("Chargebee");
        System.out.println(chargebeeTransactions);
        System.out.println("Gateway");
        System.out.println(gatewayTransactions);
        System.out.println("Accounting Software");
        System.out.println(accSoftTransactions);

        MismatchedTransactions computer = new MismatchedTransactions();
        Map<String, List<Transaction>> result = computer.mismatched(chargebeeTransactions, gatewayTransactions, accSoftTransactions);

        List<Transaction> matches = result.get("matches");
        List<Transaction> onlyInGateway = result.get("onlyInGateway");
        List<Transaction> onlyInAccSoft = result.get("onlyInAccSoft");
        List<Transaction> notInBoth = result.get("notInBoth");

        System.out.println();
        System.out.println("Matches");
        System.out.println(matches);

        System.out.println("onlyInGateway");
        System.out.println(onlyInGateway);

        System.out.println("onlyInAccSoft");
        System.out.println(onlyInAccSoft);

        System.out.println("notInBoth");
        System.out.println(notInBoth);








    }
}
