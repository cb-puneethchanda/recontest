package com.cb.reconciliation.model;

public class ChargebeeCredentials {
    private String siteName;
    private String APIKey;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public ChargebeeCredentials(String siteName, String APIKey) {
        this.siteName = siteName;
        this.APIKey = APIKey;
    }
}
