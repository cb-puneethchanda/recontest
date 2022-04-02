package com.cb.reconciliation.model;

public class JobFilter {

    private String siteUrl;

    public JobFilter() {
    }

    @Override
    public String toString() {
        return "JobFilter{" +
                "siteUrl='" + siteUrl + '\'' +
                '}';
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public JobFilter(String siteUrl) {
        this.siteUrl = siteUrl;
    }


}
