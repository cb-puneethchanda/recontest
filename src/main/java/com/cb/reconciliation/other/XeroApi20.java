//package com.cb.reconciliation;
//
//import com.github.scribejava.core.builder.api.DefaultApi20;
//import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
//import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
//
///**
// * Xero.com Api
// */
//public class XeroApi20 extends DefaultApi20 {
//
//    protected XeroApi20() {
//    }
//
//    private static class InstanceHolder {
//        private static final XeroApi20 INSTANCE = new XeroApi20();
//    }
//
//    public static XeroApi20 instance() {
//        return InstanceHolder.INSTANCE;
//    }
//
//    @Override
//    public String getAccessTokenEndpoint() {
//        return "https://identity.xero.com/connect/token";
//    }
//
//    @Override
//    protected String getAuthorizationBaseUrl() {
//        return "https://login.xero.com/identity/connect/authorize";
//    }
//
//    @Override
//    public ClientAuthentication getClientAuthentication() {
//        return RequestBodyAuthenticationScheme.instance();
//    }
//}
