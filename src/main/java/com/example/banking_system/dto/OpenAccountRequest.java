package com.example.banking_system.dto;

import com.example.banking_system.dao.entity.AccountType;
import jakarta.validation.constraints.NotNull;

public class OpenAccountRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private AccountType accountType;

    public Long getClientId() {
        return clientId;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
