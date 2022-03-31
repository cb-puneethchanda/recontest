package com.cb.reconciliation.service;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MismatchedTransactions {
    public List<Transaction> compareTransactions(
            List<Transaction> chargebeeTransactions,
            List<Transaction> gatewayTransactions,
            List<Transaction> accSoftTransactions,
            boolean onlyMismatched) {
        List<Transaction> matchingTransactions = new ArrayList<>();
        List<Transaction> mismatchedTransactions = new ArrayList<>();

        boolean inGateway=false, inAccSoft=false;

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

        if (onlyMismatched){
            return mismatchedTransactions;
        }
        else {
            return Stream.concat(matchingTransactions.stream(), mismatchedTransactions.stream()).collect(Collectors.toList());
        }
    }

    public List<Transaction> mismatched(
            ChargebeeCredentials chargebeeCredentials,
            Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap,
            Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap,
            Timestamp startTimestamp,
            Timestamp endTimestamp
            ) throws Exception {
        List<Transaction> finalList = new ArrayList<>();

        for (Map.Entry<GatewayEnum, GatewayCredentials> gatewayCredMap: gatewayCredentialsMap.entrySet()) {
            ChargebeeConnect chargebeeConnect = new ChargebeeConnect();
            List<Transaction> chargebeeTransactions = chargebeeConnect.getTransactionsByGateway(
                    chargebeeCredentials,
                    gatewayCredMap.getKey(),
                    startTimestamp,
                    endTimestamp);

            XeroConnect xeroConnect = new XeroConnect();
            List<Transaction> accSoftTransactions = xeroConnect.getTranscations((XeroCredentials) accSoftCredentialsMap.get(AccSoftEnum.XERO), startTimestamp, startTimestamp);

            List<Transaction> gatewayTransactions = null;
            switch (gatewayCredMap.getKey()) {
                case STRIPE:
                    StripeConnect stripeConnect = new StripeConnect();
                    gatewayTransactions = stripeConnect.getBalanceTransaction((StripeCredentials) gatewayCredMap.getValue(), startTimestamp, endTimestamp);
                    break;
            }

            List<Transaction> comparedTransactions = compareTransactions(chargebeeTransactions, gatewayTransactions, accSoftTransactions, true);
            System.out.println(comparedTransactions);
            finalList = Stream.concat(finalList.stream(), comparedTransactions.stream())
                    .collect(Collectors.toList());

        }
        System.out.println("FINAL");
        System.out.println(finalList);
        return finalList;
    }
}
