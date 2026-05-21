package ua.chekmaryov.barber_stat.service.clients;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Client;
import ua.chekmaryov.barber_stat.enums.ClientStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.ClientMapper;
import ua.chekmaryov.barber_stat.repository.ClientRepository;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }


    @Override
    @Transactional
    public ClientDtoResponse create(ClientDtoCreateRequest request) {
        log.info("Request to create a new Client: {} {}", request.firstName(),request.lastName());
        if(clientRepository.existsByPhone(request.phone().replaceAll("\\s+",""))){
            throw new AlreadyExistsException("Client with " + request.phone() +" already exists");
        }
        Client client = clientRepository.save(clientMapper.dtoToEntity(request));
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public Page<ClientDtoResponse> getAll(Pageable pageable) {
        log.info("Request to fetch all clients");
        Page<Client> allClients = clientRepository.findAll(pageable);
        log.debug("Retrieved {} records from database",allClients.getTotalElements());
        return allClients.map(clientMapper::toResponse);
    }

    @Override
    @Transactional
    public ClientDtoResponse getById(Long id) {
        log.info("Searching for client with id: {}" ,id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        log.debug("Successfully found client: {} (ID: {})", client.getLastName(), id);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public ClientDtoResponse updateById(Long id, ClientDtoUpdateRequest request) {
        log.info("Updating client with ID: {}",id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        if (!Objects.equals(client.getPhone(), request.phone().replaceAll("\\s+",""))){
            if(clientRepository.existsByPhone(request.phone().replaceAll("\\s+",""))){
                throw new AlreadyExistsException("Client from request with " + request.phone() +" already exists");
            }
        }
        Client updated = clientRepository.save(clientMapper.dtoUpdateToEntity(request,client));
        log.debug("Client ID {} successfully updated", id);
        return clientMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ClientDtoResponse deleteById(Long id) {
        log.info("Attempting to soft delete client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        log.debug("Client with ID: {} was found ",id);
        client.setStatus(ClientStatus.ARCHIVED);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public ClientDtoResponse getByPhone(String phone) {
        log.info("Searching for a client by a phone: {}", phone);
        Client client = clientRepository.findClientByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with phone: " + phone));
        log.debug("Client was found with a phone: {} ", phone);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public Page<ClientDtoResponse> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        log.info("Searching for clients with filter: firstName={}, lastName={}", firstName, lastName);
        Page<ClientDtoResponse> clients = clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName,lastName, pageable)
                .map(clientMapper::toResponse);
        log.debug("Clients was found with firstname {} and last name {}",firstName,lastName);
        return clients;
    }

    @Override
    @Transactional
    public Page<ClientDtoResponse> findByStatusAndLastVisitDateBetween(ClientStatus status, LocalDate lastVisitDateAfter, LocalDate lastVisitDateBefore, Pageable pageable) {
        log.info("Attempting to find client between {} and {}", lastVisitDateAfter,lastVisitDateBefore);
        if (lastVisitDateAfter.isAfter(lastVisitDateBefore)){
            throw new BadRequestException("Start date (" + lastVisitDateAfter + ") cannot be after end date (" + lastVisitDateBefore + ")");
        }
        if ((LocalDate.now().isBefore(lastVisitDateAfter) || ((LocalDate.now().isBefore(lastVisitDateBefore))))){
            throw new BadRequestException("Search dates cannot be in the future. Today is " + LocalDate.now());
        }
        Page<ClientDtoResponse> clients = clientRepository.findClientsByStatusAndLastVisitDateBetween(status,lastVisitDateAfter,lastVisitDateBefore,pageable)
                .map(clientMapper::toResponse);
        log.debug("Search complete. Found {} clients matching the criteria", clients.getTotalElements());
        return clients;
    }
}