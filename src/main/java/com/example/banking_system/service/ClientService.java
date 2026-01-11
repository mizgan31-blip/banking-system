package com.example.banking_system.service;

import com.example.banking_system.domain.Client;
import com.example.banking_system.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }
    public Client getById(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

    }
    @Transactional
    public Client create(String firstName, String lastName, String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Client client = new Client(firstName, lastName, email);
        return clientRepository.save(client);
    }
}
