package com.cb.reconciliation.service;

import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.XeroCredentials;
import com.cb.reconciliation.utils.ParseTransaction;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class XeroConnect {
    RestTemplate restTemplate = new RestTemplate();
    private final String xeroPaymentAPI = "https://api.xero.com/api.xro/2.0/Payments";

    public String getTransactionURI(String idAtGateway, String chargebeeTxnId, LocalDate afterDate) {
        String referenceID = "PG_ID%3A" + idAtGateway + "%20%7C%20CB_ID%3A" + chargebeeTxnId;
        String queryParams = "?where%3DReference%3D%3D%22" + referenceID + "%22"
                + "%20AND%20Date%3E"
                + "DateTime(" + afterDate.getYear()
                + "%2C%20" + afterDate.getMonthValue()
                + "%2C%20" + afterDate.getDayOfMonth()
                + ")";

        return xeroPaymentAPI + queryParams;
    }

    public Transaction getTranscation(
            XeroCredentials credentials,
            String idAtGateway,
            String chargebeeTxnId,
            LocalDate afterDate) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("xero-tenant-id", credentials.getXeroTenantId());
        headers.set("Authorization", "Bearer " + credentials.getAccessToken());
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API to xero
        String uri = getTransactionURI(idAtGateway, chargebeeTxnId, afterDate);
        System.out.println("GET " + uri);
        String jsonResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

        // Parse and Extract
        List<Transaction> xeroTransactions = ParseTransaction.xero(jsonResponse, "default");

        return xeroTransactions.get(0);
    }
}
