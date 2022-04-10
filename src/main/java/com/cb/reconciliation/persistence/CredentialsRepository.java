package com.cb.reconciliation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository
        extends JpaRepository<Credentials, String> {
    @Query(value = "SELECT * FROM Credentials c WHERE c.Id = ?1", nativeQuery = true)
    Credentials findCredentialsById(String Id);


}
