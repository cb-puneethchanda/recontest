package com.example.recontest.models;

public class XeroCredentials {
    private String accessToken;
    private String xeroTenantId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getXeroTenantId() {
        return xeroTenantId;
    }

    public void setXeroTenantId(String xeroTenantId) {
        this.xeroTenantId = xeroTenantId;
    }

    public XeroCredentials(String accessToken, String xeroTenantId) {
        this.accessToken = accessToken;
        this.xeroTenantId = xeroTenantId;
    }
}