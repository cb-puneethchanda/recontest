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
            List<Transaction> accSoftTransactions) {
        List<Transaction> comparedTransactions = new ArrayList<>();

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
                comparedTransactions.add(chargebeeTransaction);
            } else if (inGateway) {
                chargebeeTransaction.setIssues("NOT_IN_ACCSOFT");
               comparedTransactions.add(chargebeeTransaction);
            } else if (inAccSoft) {
                chargebeeTransaction.setIssues("NOT_IN_GATEWAY");
                comparedTransactions.add(chargebeeTransaction);
            } else {
                chargebeeTransaction.setIssues("NOT_IN_BOTH");
                comparedTransactions.add(chargebeeTransaction);
            }
        }
        return comparedTransactions;
    }

    public List<Transaction> mismatched(
            ChargebeeCredentials chargebeeCredentials,
            Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap,
            Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap,
            LocalDateTime startDate,
            LocalDateTime endDate
            ) throws Exception {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        List<Transaction> finalList = new ArrayList<>();

        for (Map.Entry<GatewayEnum, GatewayCredentials> gatewayCredMap: gatewayCredentialsMap.entrySet()) {
            ChargebeeConnect chargebeeConnect = new ChargebeeConnect();
            List<Transaction> chargebeeTransactions = chargebeeConnect.getTransactionsByGateway(
                    chargebeeCredentials,
                    gatewayCredMap.getKey(),
                    startTimestamp,
                    endTimestamp);

            XeroConnect xeroConnect = new XeroConnect();
            List<Transaction> accSoftTransactions = xeroConnect.getTranscations((XeroCredentials) accSoftCredentialsMap.get(AccSoftEnum.XERO), startDate, endDate);

            List<Transaction> gatewayTransactions = null;
            switch (gatewayCredMap.getKey()) {
                case STRIPE:
                    StripeConnect stripeConnect = new StripeConnect();
                    gatewayTransactions = stripeConnect.getBalanceTransaction((StripeCredentials) gatewayCredMap.getValue(), startTimestamp, endTimestamp);
                    break;
            }

            List<Transaction> comparedTransactions = compareTransactions(chargebeeTransactions, gatewayTransactions, accSoftTransactions);
            System.out.println(comparedTransactions);
            finalList = Stream.concat(finalList.stream(), comparedTransactions.stream())
                    .collect(Collectors.toList());

        }
        System.out.println("FINAL");
        System.out.println(finalList);
        return finalList;
    }
}
