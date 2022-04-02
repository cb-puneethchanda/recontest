package com.cb.reconciliation.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
