package com.cb.reconciliation.model;

import java.util.ArrayList;
import java.util.List;

public class ComparedTransactions {
    private List<Transaction> matches = new ArrayList<>();
    private List<Transaction> onlyInGateway = new ArrayList<>();
    private List<Transaction> onlyInAccSoft = new ArrayList<>();
    private List<Transaction> notInBoth = new ArrayList<>();

    public List<Transaction> getMatches() {
        return matches;
    }

    public void setMatches(List<Transaction> matches) {
        this.matches = matches;
    }

    public List<Transaction> getOnlyInGateway() {
        return onlyInGateway;
    }

    public void setOnlyInGateway(List<Transaction> onlyInGateway) {
        this.onlyInGateway = onlyInGateway;
    }

    public List<Transaction> getOnlyInAccSoft() {
        return onlyInAccSoft;
    }

    public void setOnlyInAccSoft(List<Transaction> onlyInAccSoft) {
        this.onlyInAccSoft = onlyInAccSoft;
    }

    public List<Transaction> getNotInBoth() {
        return notInBoth;
    }

    public void setNotInBoth(List<Transaction> notInBoth) {
        this.notInBoth = notInBoth;
    }

    public void addToMatches(Transaction transaction) {
        this.matches.add(transaction);
    }

    public void addToOnlyInGateway(Transaction transaction) {
        this.onlyInGateway.add(transaction);
    }

    public void addToOnlyInAccSoft(Transaction transaction) {
        this.onlyInAccSoft.add(transaction);
    }

    public void addToNotInBoth(Transaction transaction) {
        this.notInBoth.add(transaction);
    }

    @Override
    public String toString() {
        return "ComparedTransactions{" +
                "matches=" + matches + "\n" +
                ", onlyInGateway=" + onlyInGateway + "\n" +
                ", onlyInAccSoft=" + onlyInAccSoft + "\n" +
                ", notInBoth=" + notInBoth + "\n" +  
                '}';
    }
}
