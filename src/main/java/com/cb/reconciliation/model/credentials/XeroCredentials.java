package com.cb.reconciliation.model.credentials;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class XeroCredentials extends AccSoftCredentials {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String xeroTenantId;
    private String accessToken = null;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getXeroTenantId() {
        return xeroTenantId;
    }

    public void setXeroTenantId(String xeroTenantId) {
        this.xeroTenantId = xeroTenantId;
    }

    public XeroCredentials(String clientId, String clientSecret, String refreshToken, String xeroTenantId) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
        this.xeroTenantId = xeroTenantId;
    }

    public XeroCredentials(String xeroTenantId, String accessToken) {
        this.xeroTenantId = xeroTenantId;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        if (accessToken == null) {
            refreshAccessToken();
        }
        return accessToken;
    }

    public void refreshAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        String refresh_token_endpoint = "https://identity.xero.com/connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.set("grant_type", "refresh_token");
        headers.set("Content-Type", "application/json");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody= new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // API to xero
        System.out.println("GET " + refresh_token_endpoint);
        String jsonResponse = restTemplate.exchange(refresh_token_endpoint, HttpMethod.POST, entity, String.class).getBody();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonResponse);
        this.accessToken = (String) jsonObject.get("access_token");

        System.out.println("XERO ACCESS TOKEN: " + accessToken);
    }


}
