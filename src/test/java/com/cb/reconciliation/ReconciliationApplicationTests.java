package com.cb.reconciliation;

import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.XeroCredentials;
import com.cb.reconciliation.service.XeroConnect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class ReconciliationApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    @Test void temp(){
        String xeroTenantId = "8940aed6-420b-4a89-be09-a9c084f05702";
        String refreshToken = "0946b62fd9b256d6b67c551d117032d3e4a8133702e91aca3cdfbc55ef4479f1";
        String clientId = "0A33E85DDDB74CA5A5D08A2A178A844A";
        String clientSecret = "H6e8vmVWfg73WeI7GQ1ZJupi0Eo4BeNW_TQxA6oMGEzpFUhl";

        String idAtGateway = "ch_3KgVfHSEMRaUmSTA0wbseA5x";
        String chargebeeTxnID = "txn_AzqYRgT0utm185Db";

        XeroConnect conn = new XeroConnect();
        XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);

        LocalDate afterDate = LocalDate.of(2020, 3, 22);

        Transaction tr = conn.getTranscation(cred, idAtGateway, chargebeeTxnID, afterDate);
        System.out.println(tr);
    }
}
