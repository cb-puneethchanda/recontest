package com.cb.reconciliation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository
        extends JpaRepository<Job, String> {
    Optional<Job> findJobByJobId(String jobId);
}
