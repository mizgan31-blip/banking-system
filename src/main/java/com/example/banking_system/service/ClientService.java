package com.example.banking_system.service;

import com.example.banking_system.dao.entity.Client;
import com.example.banking_system.dao.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

    }
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client create(String firstName, String lastName, String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Client client = new Client(firstName, lastName, email);
        return clientRepository.save(client);
    }
    @Transactional
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client not found");
        }
        clientRepository.deleteById(id);
    }

}
