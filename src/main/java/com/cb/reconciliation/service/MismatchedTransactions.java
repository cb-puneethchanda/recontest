package com.cb.reconciliation.service;

import com.cb.reconciliation.model.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MismatchedTransactions {
    public ComparedTransactions compareTransactions(
            List<Transaction> chargebeeTransactions,
            List<Transaction> gatewayTransactions,
            List<Transaction> accSoftTransactions) {
        ComparedTransactions comparedTransactions = new ComparedTransactions();

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
                comparedTransactions.addToMatches(chargebeeTransaction);
            } else if (inGateway) {
               comparedTransactions.addToOnlyInGateway(chargebeeTransaction);
            } else if (inAccSoft) {
                comparedTransactions.addToOnlyInAccSoft(chargebeeTransaction);
            } else {
                comparedTransactions.addToNotInBoth(chargebeeTransaction);
            }
        }
        return comparedTransactions;
    }

    public void mismatched(
            List<GatewayEnum> gatewayEnumList,
            ChargebeeCredentials chargebeeCredentials,
            StripeCredentials stripeCredentials,
            XeroCredentials xeroCredentials,
            LocalDate startDate,
            LocalDate endDate
            ) throws Exception {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay());

        for (GatewayEnum gatewayEnumVal: gatewayEnumList) {
            ChargebeeConnect chargebeeConnect = new ChargebeeConnect();
            List<Transaction> chargebeeTransactions = chargebeeConnect.getTransactionsByGateway(chargebeeCredentials, gatewayEnumVal, startTimestamp, endTimestamp);

            XeroConnect xeroConn = new XeroConnect();
            List<Transaction> accSoftTransactions = xeroConn.getTranscations(xeroCredentials, startDate, endDate);

            StripeConnect stripeConnect = new StripeConnect();
            List<Transaction> gatewayTransactions = stripeConnect.getTransactions(stripeCredentials, startTimestamp, endTimestamp);

            ComparedTransactions comparedTransactions = compareTransactions(chargebeeTransactions, gatewayTransactions, accSoftTransactions);

            System.out.println(comparedTransactions);
        }
    }
}
