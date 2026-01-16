package com.example.banking_system.service;

import com.example.banking_system.domain.*;
import com.example.banking_system.repository.AccountRepository;
import com.example.banking_system.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.banking_system.repository.TransactionRepository;

import java.math.BigDecimal;
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

    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");

        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new IllegalArgumentException("Account is blocked");
        }
        //обновляем баланс
        account.deposit(amount);

        //создаем транзакцию
        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                account,
                null
        );
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new IllegalArgumentException("Account is blocked");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.withdraw(amount);

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAW,
                amount,
                account,
                null
        );

        transactionRepository.save(transaction);
    }

    @Transactional
    public void transfer(Long fromAccountId,
                         Long toAccountId,
                         BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Account must be positive");
        }
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Account must be diffrent");
        }
        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));

        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        if (from.getStatus() == AccountStatus.BLOCKED || (to.getStatus() == AccountStatus.BLOCKED)) {
            throw new IllegalArgumentException("Account is blocked");
        }
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        from.withdraw(amount);
        to.deposit(amount);

        Transaction transaction = new Transaction(
                TransactionType.TRANSFER,
                amount,
                from,
                to
        );
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAccountHistory(Long accountId) {

        accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId);
    }


    //Получить счет по Id
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    //Счет клиента
    public List<Account> getClientAccounts(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    //Открыть счет
    @Transactional
    public Account openAccount(Long clientId, AccountType accountType) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        String accountNumber = generateAccountNumber();

        Account account = new Account(client, accountType, accountNumber);
        return accountRepository.save(account);
    }

    //Заблокировать счет
    @Transactional
    public void blockAccount(Long accountId) {
        Account account = getById(accountId);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            return;

        }
        account.block();
        accountRepository.save(account);
    }

    //Генерация  номера счета
    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}
