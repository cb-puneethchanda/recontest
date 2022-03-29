package com.cb.reconciliation.model;

public class StripeCredentials {
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
