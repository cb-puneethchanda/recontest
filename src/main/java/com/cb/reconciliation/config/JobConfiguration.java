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
            List<Job> l = new ArrayList<Job>();
            repository.saveAll(l);
        };
    }
}
