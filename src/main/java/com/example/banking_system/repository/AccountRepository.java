package com.example.banking_system.repository;

import com.example.banking_system.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account,Long>{

    List<Account> findByClientId(Long clientId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
