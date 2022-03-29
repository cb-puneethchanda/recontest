package com.cb.reconciliation.service;

import com.cb.reconciliation.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MismatchedTransactions {
    public Map<String, List<Transaction>> mismatched(
            List<Transaction> chargebeeTransactions,
            List<Transaction> gatewayTransactions,
            List<Transaction> accSoftTransactions) {
        List<Transaction> matches = new ArrayList<>();
        List<Transaction> onlyInGateway = new ArrayList<>();
        List<Transaction> onlyInAccSoft = new ArrayList<>();
        List<Transaction> notInBoth = new ArrayList<>();

        boolean inGateway=false, inAccSoft=false;

        for (int i = 0; i < chargebeeTransactions.size(); i++) {
            inGateway = false;

            for (int j = 0; j < gatewayTransactions.size(); j++) {
                if (chargebeeTransactions.get(i).equals(gatewayTransactions.get(j))) {
                    inGateway = true;
                    inAccSoft = false;

                    for (int k = 0; k < accSoftTransactions.size(); k++) {
                        if (chargebeeTransactions.get(i).equals(accSoftTransactions.get(k))) {
                            inAccSoft = true;
                            break;
                        }
                    }
                }
            }

            if (inGateway && inAccSoft) {
                matches.add(chargebeeTransactions.get(i));
            } else if (inGateway) {
                onlyInGateway.add(chargebeeTransactions.get(i));
            } else if (inAccSoft) {
                onlyInAccSoft.add(chargebeeTransactions.get(i));
            } else {
                notInBoth.add(chargebeeTransactions.get(i));
            }
        }

        Map<String, List<Transaction>> result = new HashMap<>();
        result.put("matches", matches);
        result.put("onlyInGateway", onlyInGateway);
        result.put("onlyInAccSoft", onlyInAccSoft);
        result.put("notInBoth", notInBoth);

        return result;

    }
}
