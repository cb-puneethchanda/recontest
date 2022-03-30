package com.cb.reconciliation.service;

import com.cb.reconciliation.model.credentials.ChargebeeCredentials;
import com.cb.reconciliation.model.GatewayEnum;
import com.cb.reconciliation.model.Transaction;
import com.chargebee.Environment;
import com.chargebee.ListResult;
import com.chargebee.filters.enums.SortOrder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChargebeeConnect {
    public List<Transaction> getTransactionsByGateway(ChargebeeCredentials credentials, GatewayEnum gatewayEnumType, Timestamp startDate, Timestamp endDate) throws Exception {
        // Choose Gateway
        com.chargebee.models.enums.Gateway gatewayEnumVal;
        switch (gatewayEnumType) {
            case STRIPE:
                gatewayEnumVal = com.chargebee.models.enums.Gateway.STRIPE;
                break;
            default:
                gatewayEnumVal = com.chargebee.models.enums.Gateway.CHARGEBEE;
        }

        // Chargebee API
        Environment.configure(credentials.getSiteName(), credentials.getAPIKey());
        ListResult result = com.chargebee.models.Transaction.list()
//                .limit(10).offset(10)
                .date().between(startDate, endDate)
                .gateway().is(gatewayEnumVal)
                .sortByDate(SortOrder.DESC)
                .request();

        List<Transaction> transactions = new ArrayList<>();
        for(ListResult.Entry entry:result) {
            com.chargebee.models.Transaction cb_transaction = entry.transaction();
            String idAtGateway = cb_transaction.idAtGateway();
            double amount = cb_transaction.amount();
            String currencyCode = cb_transaction.currencyCode();
            LocalDateTime date = cb_transaction.date().toLocalDateTime();

            Transaction tr = new Transaction(idAtGateway, date, amount, currencyCode);
            transactions.add(tr);
        }
//        System.out.println("CB");
//        System.out.println(transactions);
        return transactions;
    }
}
