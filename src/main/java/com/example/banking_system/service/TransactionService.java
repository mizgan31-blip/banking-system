package com.example.banking_system.service;

import com.example.banking_system.dao.entity.Account;
import com.example.banking_system.dao.entity.AccountStatus;
import com.example.banking_system.dao.entity.Transaction;
import com.example.banking_system.dao.entity.TransactionType;
import com.example.banking_system.dao.repository.AccountRepository;
import com.example.banking_system.dao.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {
        log.info("Пополнение счета:accountId{}, amount{}", accountId, amount);
        validateAmount(amount);

        Account account = getActiveAccount(accountId);
        account.deposit(amount);

        transactionRepository.save(
                new Transaction(TransactionType.DEPOSIT, amount, account, null)
        );
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {
        log.info("Снятие средств:accountId{}, amount{}", accountId, amount);
        validateAmount(amount);

        Account account = getActiveAccount(accountId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.withdraw(amount);

        transactionRepository.save(
                new Transaction(TransactionType.WITHDRAW, amount, account, null)
        );
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        log.info("Перевод средств:from{},to{], amount{}", fromAccountId, toAccountId, amount);
        validateAmount(amount);

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Accounts must be different");
        }

        Account from = getActiveAccount(fromAccountId);
        Account to = getActiveAccount(toAccountId);

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.withdraw(amount);
        to.deposit(amount);

        transactionRepository.save(
                new Transaction(TransactionType.TRANSFER, amount, from, to)
        );
    }

    public List<Transaction> getAccountHistory(Long accountId) {
        log.info("Запрос историй операций:accountId{}", accountId);
        accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private Account getActiveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new IllegalArgumentException("Account is blocked");
        }

        return account;
    }
}

