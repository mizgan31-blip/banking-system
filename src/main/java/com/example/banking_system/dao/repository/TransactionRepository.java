package com.example.banking_system.dao.repository;

import com.example.banking_system.dao.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionRepository  extends JpaRepository<Transaction, Long>{

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}

