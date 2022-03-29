package com.example.recontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.recontest.models.ParseTransaction;
import com.example.recontest.models.XeroCredentials;
import com.example.recontest.models.XeroTransaction;

import java.util.List;

@Service
public class XeroConnect {
    RestTemplate restTemplate = new RestTemplate();
    private String xeroPaymentAPI = "https://api.xero.com/api.xro/2.0/Payments";

    public String getTransactionURI(String idAtGateway, String chargebeeTxnId){
        String referenceID = "PG_ID%3A" + idAtGateway + "%20%7C%20CB_ID%3A" + chargebeeTxnId;
        return xeroPaymentAPI + "?where%3DReference%3D%3D%22" + referenceID + "%22";
    }

    public XeroTransaction getTranscation(XeroCredentials credentials, String idAtGateway, String chargebeeTxnId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("xero-tenant-id", credentials.getXeroTenantId());
        headers.set("Authorization", "Bearer " + credentials.getAccessToken());
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API to xero
        String uri = getTransactionURI(idAtGateway, chargebeeTxnId);
//        System.out.println("GET " + uri);
        String jsonResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

        // Parse and Extract
        List<XeroTransaction> xeroTransactions = ParseTransaction.xero(jsonResponse, "default");

        return xeroTransactions.get(0);
    }
}