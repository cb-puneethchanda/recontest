package com.cb.reconciliation.model.credentials;

public class StripeCredentials extends GatewayCredentials{
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public StripeCredentials(String apiKey) {
        this.apiKey = apiKey;
    }
}
