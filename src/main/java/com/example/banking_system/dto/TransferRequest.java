package com.example.banking_system.dto;

import java.math.BigDecimal;

public class TransferRequest {

    private Long targetAcountId;
    private BigDecimal amount;

    public Long getTargetAccountId() {
        return targetAcountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
