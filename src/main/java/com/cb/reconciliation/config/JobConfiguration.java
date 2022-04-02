package com.cb.reconciliation.config;

import com.cb.reconciliation.persistence.Job;
import com.cb.reconciliation.persistence.JobRepository;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class JobConfiguration {
    @Bean
    CommandLineRunner commandLineRunner(JobRepository repository) {
        return args -> {
            JSONObject json = new JSONObject("{\n" +
                    "    \"mismatched\": [\n" +
                    "        {\n" +
                    "            \"date\": \"2022-03-30T21:06:22\",\n" +
                    "            \"transactionType\": \"refund\",\n" +
                    "            \"amount\": 29900.0,\n" +
                    "            \"id\": \"re_3Kj0geSFiiJc1ZKR0w3iP1Us\",\n" +
                    "            \"currencyCode\": \"INR\",\n" +
                    "            \"issues\": \"NONE\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"metadata\": {}\n" +
                    "}");

            Job newJob = Job.builder()
//                    .jobId(UUID.randomUUID().toString())
                    .jobId("1")
                    .chargebeeSiteUrl("siteurl")
                    .gateway("stripe")
                    .startTime(Timestamp.valueOf(LocalDateTime.of(2022, 3, 22, 0, 0)))
                    .endTime((Timestamp.valueOf(LocalDateTime.now())))
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .status("PROCESSING")
                    .mismatched(json)
                    .build();

            List<Job> l = new ArrayList<Job>();
            l.add(newJob);
            repository.saveAll(l);
        };
    }
}
