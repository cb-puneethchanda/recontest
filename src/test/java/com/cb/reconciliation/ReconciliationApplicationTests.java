package com.cb.reconciliation;

import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.XeroCredentials;
import com.cb.reconciliation.service.XeroConnect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReconciliationApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    @Test void temp(){
        String access_token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg1NDc4NjMsImV4cCI6MTY0ODU0OTY2MywiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMEEzM0U4NUREREI3NENBNUE1RDA4QTJBMTc4QTg0NEEiLCJzdWIiOiIxODM1Mjg5OWZjZDU1OTU1Yjk0ZmIwNmNiMzQ0Y2Y2NSIsImF1dGhfdGltZSI6MTY0ODQ0OTc3MCwieGVyb191c2VyaWQiOiJjMWIwNGI1ZC1kNWJjLTRhYzItODMxMy00OTgyMjNmZGM3ZDEiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjhmNDhiZmU0OWI0NDQ1NDU5ZjlkZWI0OWJhYjFmODAxIiwianRpIjoiNGI4OWNmMDQ5M2Q3NjAwYWVhOGEzNTcxNjgyZWVlZjMiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjhmZWNjNTZiLWQ1MDAtNDBjZS1hM2QwLTEzOTJmODZjNWMxMSIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsImFjY291bnRpbmcuY29udGFjdHMiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.w-1loOi4HutJ3eYriH3lNj5x8KO5hOMUNS7UNNS8ejaIadrmQSP6VQ6KPoILBhizJ9o-xMCsQBdGMh-EtqCBy3NgIxcsSfOTjLXps_nWKlSZSGl-aLVStpvWRTffP8HnVhEtbsFfFDo5bozhRWtx5aayXyMm8LC7ZvdiqkDQ4rgM9vZguijthXfwYFWRn4XgjXCUfcaN5PVMCGojflxG9vREbccESWDQjZmFmRfeV7OsP1RlwjNfzddDz2DN-oVkqs3JCF5zl1XwiDV7aNkmh8je1vd9NL0QADWJO0Xc2l4NgGetPiFhxWcDdW20Vgp30kiwPPk7_eTNSVMNIcnHoA";
        String xero_tenant_id = "8940aed6-420b-4a89-be09-a9c084f05702";
        String idAtGateway = "ch_3KgVfHSEMRaUmSTA0wbseA5x";
        String chargebeeTxnID = "txn_AzqYRgT0utm185Db";

        XeroConnect conn = new XeroConnect();
        XeroCredentials cred = new XeroCredentials(access_token, xero_tenant_id);

        Transaction tr = conn.getTranscation(cred, idAtGateway, chargebeeTxnID);
        System.out.println(tr);
    }
}
