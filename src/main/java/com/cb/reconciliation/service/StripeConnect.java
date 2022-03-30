package com.cb.reconciliation.service;

import com.cb.reconciliation.model.credentials.StripeCredentials;
import com.cb.reconciliation.model.Transaction;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeConnect {
    public List<Transaction> getTransactions(StripeCredentials credentials, Timestamp startDate, Timestamp endDate) throws StripeException {
        Map<String, Long> dateMap = new HashMap<>();
        dateMap.put("gte", (long) (startDate.getTime() / 1000));
        dateMap.put("lte", (long) (endDate.getTime() / 1000));

        Stripe.apiKey = credentials.getApiKey();

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);
        params.put("created", dateMap);

        ChargeCollection charges = Charge.list(params);

        List<Transaction> transactions = new ArrayList<>();
        for (Charge charge: charges.getData()) {
            String idAtGateway = charge.getId();
            double amount = charge.getAmount();
            String currencyCode = charge.getCurrency();
            long epochTime = charge.getCreated();
//            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTime), ZoneId.systemDefault());
            LocalDateTime date = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC);

            Transaction tr = new Transaction(idAtGateway, date, amount, currencyCode);
            transactions.add(tr);
        }
//        System.out.println("ST");
//        System.out.println(transactions);
        return transactions;
    }

    public List<Transaction> getRefunds(StripeCredentials credentials, Timestamp startDate, Timestamp endDate) throws StripeException {
        Map<String, Long> dateMap = new HashMap<>();
        dateMap.put("gte", (long) (startDate.getTime() / 1000));
        dateMap.put("lte", (long) (endDate.getTime() / 1000));

        Stripe.apiKey = credentials.getApiKey();

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);
        params.put("created", dateMap);

        RefundCollection refunds = Refund.list(params);

        List<Transaction> transactions = new ArrayList<>();
        for (Refund refund: refunds.getData()) {
            // todo check
            String idAtGateway = refund.getId();
            double amount = refund.getAmount();
            String currencyCode = refund.getCurrency();
            long epochTime = refund.getCreated();
            LocalDateTime date = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC);

//            refund.ge();

            Transaction tr = new Transaction(idAtGateway, date, amount, currencyCode);
            transactions.add(tr);
        }
        return transactions;
    }

    public List<Transaction> getBalanceTransaction(StripeCredentials credentials, Timestamp startDate, Timestamp endDate) throws StripeException {
        Map<String, Long> dateMap = new HashMap<>();
        dateMap.put("gte", (long) (startDate.getTime() / 1000));
        dateMap.put("lte", (long) (endDate.getTime() / 1000));

        Stripe.apiKey = credentials.getApiKey();

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);
        params.put("created", dateMap);

        BalanceTransactionCollection balanceTransactionCollection = BalanceTransaction.list(params);

        List<Transaction> transactions = new ArrayList<>();
        for (BalanceTransaction balanceTransaction: balanceTransactionCollection.getData()) {
            String idAtGateway = balanceTransaction.getSource();
            double amount = balanceTransaction.getAmount();
            String currencyCode = balanceTransaction.getCurrency();
            long epochTime = balanceTransaction.getCreated();
            LocalDateTime date = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC);

            Transaction tr = new Transaction(idAtGateway, date, amount, currencyCode);
            transactions.add(tr);
        }
//        System.out.println("ST");
//        System.out.println(transactions);
        return transactions;
    }

}
