package com.example.banking_system.controller;

import com.example.banking_system.dao.entity.Account;
import com.example.banking_system.dto.OpenAccountRequest;
import com.example.banking_system.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account openAccount(@Valid @RequestBody OpenAccountRequest request) {
        return accountService.openAccount(
                request.getClientId(),
                request.getAccountType()
        );
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<Account> getClientAccounts(@PathVariable Long clientId) {
        return accountService.getClientAccounts(clientId);
    }

    @PutMapping("/{id}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void block(@PathVariable Long id) {
        accountService.blockAccount(id);
       }
     }

