package com.example.banking_system.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    //Счет,с которого списываем / на который кладем
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Для TRANSFER (может быть null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Transaction() {

    }

    public Transaction(TransactionType type,
                       BigDecimal amount,
                       Account account,
                       Account targetAccount) {

        this.type = type;
        this.amount = amount;
        this.account = account;
        this.targetAccount = targetAccount;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
