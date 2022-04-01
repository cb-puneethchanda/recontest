package com.cb.reconciliation.service;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MismatchedTransactionsService {
    public List<Transaction> compareTransactions(
            List<Transaction> chargebeeTransactions,
            List<Transaction> gatewayTransactions,
            List<Transaction> accSoftTransactions,
            boolean onlyMismatched) {
        List<Transaction> matchingTransactions = new ArrayList<>();
        List<Transaction> mismatchedTransactions = new ArrayList<>();

        boolean inGateway = false, inAccSoft = false;

        for (int i = 0; i < chargebeeTransactions.size(); i++) {
            Transaction chargebeeTransaction = chargebeeTransactions.get(i);
            inGateway = false;

            for (int j = 0; j < gatewayTransactions.size(); j++) {
                if (chargebeeTransaction.equals(gatewayTransactions.get(j))) {
                    inGateway = true;
                    inAccSoft = false;

                    for (int k = 0; k < accSoftTransactions.size(); k++) {
                        if (chargebeeTransaction.equals(accSoftTransactions.get(k))) {
                            inAccSoft = true;
                            break;
                        }
                    }
                }
            }
            if (inGateway && inAccSoft) {
                chargebeeTransaction.setIssues("NONE");
                matchingTransactions.add(chargebeeTransaction);
            } else if (inGateway) {
                chargebeeTransaction.setIssues("NOT_IN_ACCSOFT");
                mismatchedTransactions.add(chargebeeTransaction);
            } else if (inAccSoft) {
                chargebeeTransaction.setIssues("NOT_IN_GATEWAY");
                mismatchedTransactions.add(chargebeeTransaction);
            } else {
                chargebeeTransaction.setIssues("NOT_IN_BOTH");
                mismatchedTransactions.add(chargebeeTransaction);
            }
        }

        if (onlyMismatched) {
            return mismatchedTransactions;
        } else {
            return Stream.concat(matchingTransactions.stream(), mismatchedTransactions.stream()).collect(Collectors.toList());
        }
    }

    @Autowired
    private ChargebeeConnectService chargebeeConnectService;
    @Autowired
    private XeroConnectService xeroConnectService;
    @Autowired
    private StripeConnectService stripeConnectService;

    @Async
    public void mismatched(
            ChargebeeCredentials chargebeeCredentials,
            Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap,
            Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap,
            Timestamp startTimestamp,
            Timestamp endTimestamp
    ) throws Exception {

        List<Transaction> finalList = new ArrayList<>();
        System.out.println(Thread.currentThread().getName());
        for (Map.Entry<GatewayEnum, GatewayCredentials> gatewayCredMap : gatewayCredentialsMap.entrySet()) {
            List<Transaction> chargebeeTransactions = chargebeeConnectService.getTransactionsByGateway(
                    chargebeeCredentials,
                    gatewayCredMap.getKey(),
                    startTimestamp,
                    endTimestamp);
            System.out.println(Thread.currentThread().getName());
            List<Transaction> accSoftTransactions = xeroConnectService.getTranscations((XeroCredentials) accSoftCredentialsMap.get(AccSoftEnum.XERO), startTimestamp, startTimestamp);

            List<Transaction> gatewayTransactions = null;
            switch (gatewayCredMap.getKey()) {
                case STRIPE:
                    gatewayTransactions = stripeConnectService.getBalanceTransaction((StripeCredentials) gatewayCredMap.getValue(), startTimestamp, endTimestamp);
                    break;
            }

            List<Transaction> comparedTransactions = compareTransactions(chargebeeTransactions, gatewayTransactions, accSoftTransactions, true);
            System.out.println(comparedTransactions);
            finalList = Stream.concat(finalList.stream(), comparedTransactions.stream())
                    .collect(Collectors.toList());

        }
        System.out.println(Thread.currentThread().getName());
        System.out.println(finalList);

        //todo: job status update and report update
    }
}
