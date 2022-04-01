package com.cb.reconciliation.model;

import java.sql.Timestamp;

public class DateModel {
    private String startDate;
    private String endDate;

    public Timestamp getStartDate() {
        Timestamp startTime = new Timestamp(Long.parseLong(this.startDate) *1000);
        return startTime;
    }

    public Timestamp getEndDate() {
        Timestamp endTime = new Timestamp(Long.parseLong(this.endDate)*1000);
        return endTime;
    }
}
