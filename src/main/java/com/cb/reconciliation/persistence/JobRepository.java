package com.cb.reconciliation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository
        extends JpaRepository<Job, String> {

}
