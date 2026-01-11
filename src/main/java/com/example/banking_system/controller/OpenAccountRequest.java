package com.example.banking_system.controller;

import com.example.banking_system.domain.AccountType;

public class OpenAccountRequest {
    private Long clientId;
    private AccountType accountType;

    public Long getClientId() {
        return clientId;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
