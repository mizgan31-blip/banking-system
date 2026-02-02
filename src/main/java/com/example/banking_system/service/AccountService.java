package com.example.banking_system.service;

import com.example.banking_system.dao.entity.*;
import com.example.banking_system.dao.repository.AccountRepository;
import com.example.banking_system.dao.repository.ClientRepository;
import com.example.banking_system.dao.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)

public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          ClientRepository clientRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public List<Account> getClientAccounts(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @Transactional
    public Account openAccount(Long clientId, AccountType accountType) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        String accountNumber = generateAccountNumber();

        Account account = new Account(client, accountType, accountNumber);
        return accountRepository.save(account);
    }

    @Transactional
    public void blockAccount(Long accountId) {
        Account account = getById(accountId);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            return;

        }
        account.block();
        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}
