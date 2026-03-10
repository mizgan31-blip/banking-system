package com.example.banking_system.dto;

import com.example.banking_system.dao.entity.AccountType;
import jakarta.validation.constraints.NotNull;

public class OpenAccountRequest {

    @NotNull
    private Long clientId;

    private AccountType accountType;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}

