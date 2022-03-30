package com.cb.reconciliation.service;

import com.cb.reconciliation.model.Transaction;
import com.cb.reconciliation.model.credentials.XeroCredentials;
import com.cb.reconciliation.utils.ParseTransaction;
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

    public String getTransactionURI(LocalDate startDate, LocalDate endDate) {
        String queryParams = "?where%3D"
                + "Date3E%3D"
                + "DateTime(" + startDate.getYear()
                + "%2C%20" + startDate.getMonthValue()
                + "%2C%20" + startDate.getDayOfMonth()
                + ")"
                + "%20AND%20Date%3C%3D"
                + "DateTime(" + endDate.getYear()
                + "%2C%20" + endDate.getMonthValue()
                + "%2C%20" + endDate.getDayOfMonth()
                + ")";

        return xeroPaymentAPI + queryParams;
    }

    public List<Transaction> getTranscations(
            XeroCredentials credentials,
            LocalDate startDate,
            LocalDate endDate) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("xero-tenant-id", credentials.getXeroTenantId());
        headers.set("Authorization", "Bearer " + credentials.getAccessToken());
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API to xero
        String uri = getTransactionURI(startDate, endDate);
        System.out.println("GET " + uri);
        String jsonResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

        // Parse and Extract
        List<Transaction> xeroTransactions = ParseTransaction.xero(jsonResponse, "default");

        return xeroTransactions;
    }
}
