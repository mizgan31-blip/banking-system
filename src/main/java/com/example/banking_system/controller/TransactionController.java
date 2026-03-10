package com.example.banking_system.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.example.banking_system.dto.AmountRequest;
import com.example.banking_system.service.TransactionService;

@RestController
@RequestMapping("/api/accounts")

public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountId}/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deposit(
            @PathVariable Long accountId,
            @RequestBody AmountRequest request
    ) {
        transactionService.deposit(accountId, request.getAmount());
    }

    @PostMapping("/{accountId}/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(
            @PathVariable Long accountId,
            @RequestBody AmountRequest request
    ) {
        transactionService.withdraw(accountId, request.getAmount());
    }

}
