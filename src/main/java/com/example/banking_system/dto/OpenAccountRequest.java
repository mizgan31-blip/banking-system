package com.example.banking_system.dto;

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

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

}
