package com.example.banking_system.controller;

import com.example.banking_system.domain.Account;
import com.example.banking_system.domain.Transaction;
import com.example.banking_system.service.AccountService;
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

    //Открыть счет
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account openAccount(@RequestBody OpenAccountRequest request) {
        return accountService.openAccount(
                request.getClientId(),
                request.getAccountType()

        );
    }
    //Получить счет по Id
    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id){
        return accountService.getById(id);
    }
    //Все счета клиента
    @GetMapping("/client/{clientId}")
    public List<Account>getClientAccounts(@PathVariable Long clientId){
        return accountService.getClientAccounts(clientId);
    }
    //Заблокировать счет
    @PutMapping("/{id}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void block(@PathVariable Long id){
        accountService.blockAccount(id);
    }
    @GetMapping("/{id}/transactions")
    public List<Transaction> getAccountHistory(@PathVariable Long id){
        return accountService.getAccountHistory(id);
    }
    //Пополнить счет
    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void deposit(@PathVariable Long id,
                         @RequestBody AmountRequest request){
        accountService.deposit(id, request.getAmount());

    }
    @PostMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable Long id,
                         @RequestBody AmountRequest request){
        accountService.withdraw(id, request.getAmount());
    }
    @PostMapping("/{id}/trasfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@PathVariable Long id,
                         @RequestBody TransferRequest request){
        accountService.transfer(id,
        request.getTargetAccountId(),
                request.getAmount()
        );

    }
}
