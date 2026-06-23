package ua.chekmaryov.barber_stat.app.clients.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.app.clients.ClientMapper;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoPostRequest;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoPatchRequest;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoPutRequest;
import ua.chekmaryov.barber_stat.app.clients.persistence.Client;
import ua.chekmaryov.barber_stat.app.clients.persistence.ClientRepository;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;

import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public Client create(ClientDtoPostRequest request) {
        log.info("Request to create a new Client: {} {}", request.firstName(), request.lastName());
        if (clientRepository.existsByPhone(request.phone().replaceAll("\\s+", ""))) {
            throw new AlreadyExistsException("Client with " + request.phone() + " already exists");
        }
        Client client = clientRepository.save(clientMapper.dtoToEntity(request));
        return client;
    }


    public Page<Client> getAll(Pageable pageable, Specification<Client> spec) {
        log.info("Request to fetch all clients");
        Page<Client> allClients = clientRepository.findAll(spec, pageable);
        log.debug("Retrieved {} records from database", allClients.getTotalElements());
        return allClients;
    }


    public Client getById(Long id) {
        log.info("Searching for client with id: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        log.debug("Successfully found client: {} (ID: {})", client.getLastName(), id);
        return client;
    }


    @Transactional
    public Client patchById(Long id, ClientDtoPatchRequest request) {
        log.info("Updating client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        if (!Objects.equals(client.getPhone(), request.phone().replaceAll("\\s+", ""))) {
            if (clientRepository.existsByPhone(request.phone().replaceAll("\\s+", ""))) {
                throw new AlreadyExistsException("Client from request with " + request.phone() + " already exists");
            }
        }
        Client updated = clientRepository.save(clientMapper.dtoToEntity(request, client));
        log.debug("Client ID {} successfully updated", id);
        return updated;
    }

    @Transactional
    public Client updateById(Long id, ClientDtoPutRequest request) {
        log.info("Updating client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        if (!Objects.equals(client.getPhone(), request.phone().replaceAll("\\s+", ""))) {
            if (clientRepository.existsByPhone(request.phone().replaceAll("\\s+", ""))) {
                throw new AlreadyExistsException("Client from request with " + request.phone() + " already exists");
            }
        }
        Client updated = clientRepository.save(clientMapper.dtoToEntity(request, id));
        return updated;

    }
}