package com.cb.reconciliation.service;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
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
            ChargebeeCredentials chargebeeCredentials,
            Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap,
            Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap,
            LocalDate startDate,
            LocalDate endDate
            ) throws Exception {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay());

        for (Map.Entry<GatewayEnum, GatewayCredentials> gatewayCredMap: gatewayCredentialsMap.entrySet()) {
            ChargebeeConnect chargebeeConnect = new ChargebeeConnect();
            List<Transaction> chargebeeTransactions = chargebeeConnect.getTransactionsByGateway(chargebeeCredentials, gatewayCredMap.getKey(), startTimestamp, endTimestamp);

            XeroConnect xeroConn = new XeroConnect();
            List<Transaction> accSoftTransactions = xeroConn.getTranscations((XeroCredentials) accSoftCredentialsMap.get(AccSoftEnum.XERO), startDate, endDate);

            List<Transaction> gatewayTransactions = null;
            switch (gatewayCredMap.getKey()) {
                case STRIPE:
                    StripeConnect stripeConnect = new StripeConnect();
                    gatewayTransactions = stripeConnect.getTransactions((StripeCredentials) gatewayCredMap.getValue(), startTimestamp, endTimestamp);
                    break;
            }

            ComparedTransactions comparedTransactions = compareTransactions(chargebeeTransactions, gatewayTransactions, accSoftTransactions);
            System.out.println(comparedTransactions);
        }
    }
}
