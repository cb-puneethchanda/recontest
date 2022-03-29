package com.cb.reconciliation.service;

import com.cb.reconciliation.model.StripeCredentials;
import com.cb.reconciliation.model.Transaction;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTime), ZoneId.systemDefault());

            Transaction tr = new Transaction(idAtGateway, date, amount, currencyCode);
            transactions.add(tr);
//            System.out.println(paymentIntent);
        }
        return transactions;
    }
}
