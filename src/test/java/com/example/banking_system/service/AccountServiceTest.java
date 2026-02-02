package com.example.banking_system.service;

import com.example.banking_system.dao.entity.*;
import com.example.banking_system.dao.repository.AccountRepository;
import com.example.banking_system.dao.repository.ClientRepository;
import com.example.banking_system.dao.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private ClientRepository clientRepository;
    private TransactionRepository transactionRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        clientRepository = mock(ClientRepository.class);
        transactionRepository = mock(TransactionRepository.class);

        accountService = new AccountService(
                accountRepository,
                clientRepository,
                transactionRepository
        );
    }
    @Test
    void deposit_shouldIncreaseBalance_andCreateTransaction() {
        // given
        Long accountId = 1L;
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal depositAmount = new BigDecimal("50.00");

        Client client = new Client(
                "Ivan",
                "Ivanov",
                "ivan@example.com"
        );
        // эмулируем то, что сделал бы JPA
        ReflectionTestUtils.setField(client, "id", 1L);

        Account account = new Account(
                client,
                AccountType.DEBIT,
                "ACC-123"
        );
        ReflectionTestUtils.setField(account, "id", accountId);


        ReflectionTestUtils.setField(account, "status", AccountStatus.ACTIVE);

        // начальный баланс через доменную логику
        account.deposit(initialBalance);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        // when
        accountService.deposit(accountId, depositAmount);

        // then
        assertEquals(
                new BigDecimal("150.00"),
                account.getBalance(),
                "Balance should be increased after deposit"
        );

        verify(accountRepository).findById(accountId);
        verify(transactionRepository).save(any(Transaction.class));
    }
    @Test
    void deposit_shouldThrowExeption_whenAmountIsNegative(){

        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("-10");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                ()-> accountService.deposit(accountId,amount)
        );
        assertEquals("Amount must be positive", ex.getMessage());

        verifyNoInteractions(accountRepository, transactionRepository);
    }
    @Test
    void deposit_shouldThrowExeption_whenAccountIsBlocked(){
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("50");

        Client client = new Client("Ivan", "Ivanov", "ivan@example.com");
        ReflectionTestUtils.setField(client,"id",1L);

        Account account = new Account(client, AccountType.DEBIT, "ACC-123");
        ReflectionTestUtils.setField(account, "id", accountId);
        ReflectionTestUtils.setField(account, "status",AccountStatus.BLOCKED);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                ()->accountService.deposit(accountId,amount)
        );
        assertEquals("Account is blocked", ex.getMessage());

        verify(accountRepository).findById(accountId);
        verifyNoInteractions(transactionRepository);

    }
    @Test
    void deposit_shouldThrowExeption_whenAccountNoFound(){
        Long accountId = 99L;
        BigDecimal amount = new BigDecimal("55");

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                ()->accountService.deposit(accountId,amount)
        );

        assertEquals("Account not found",ex.getMessage());

        verify(accountRepository).findById(accountId);
        verifyNoInteractions(transactionRepository);


    }

}
