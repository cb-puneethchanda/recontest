package com.cb.reconciliation.model;

import lombok.Builder;
import org.json.simple.JSONObject;

import java.time.LocalDate;

//@Builder
public class XeroTransaction extends Transaction{
    public XeroTransaction(String id){
        super(id);
    }

    public XeroTransaction(String id, LocalDate date, double amount, String currencyCode) {
        super(id, date, amount, currencyCode);
    }


}
