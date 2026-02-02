package com.example.banking_system.controller;

import com.example.banking_system.dao.entity.Client;
import com.example.banking_system.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")

public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        return clientService.create(
                client.getFirstName(),
                client.getLastName(),
                client.getEmail()
        );

    }
}
