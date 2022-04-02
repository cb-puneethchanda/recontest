package com.cb.reconciliation.persistence;

import lombok.*;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//@Setter
public class Job {
    @Id
    private String jobId;
    @NonNull
    private String chargebeeSiteUrl;
    @NonNull
    private String gateway;
    @NonNull
    private String status;

    @Column(columnDefinition = "TEXT")
    @Convert(converter= JSONObjectConverter.class)
    private JSONObject mismatched;

    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createdAt;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @NonNull
    public String getChargebeeSiteUrl() {
        return chargebeeSiteUrl;
    }

    public void setChargebeeSiteUrl(@NonNull String chargebeeSiteUrl) {
        this.chargebeeSiteUrl = chargebeeSiteUrl;
    }

    @NonNull
    public String getGateway() {
        return gateway;
    }

    public void setGateway(@NonNull String gateway) {
        this.gateway = gateway;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public JSONObject getMismatched() {
        return mismatched;
    }

    public void setMismatched(JSONObject mismatched) {
        this.mismatched = mismatched;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
