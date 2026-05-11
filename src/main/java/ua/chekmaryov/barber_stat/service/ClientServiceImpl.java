package ua.chekmaryov.barber_stat.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Client;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.mapper.ClientMapper;
import ua.chekmaryov.barber_stat.repository.ClientRepository;

import java.time.LocalDate;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService{
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
        if(clientRepository.existsByPhone(request.phone())){
            throw new AlreadyExistsException("Client with " + request.phone() +" already exists");
        }
        Client client = clientRepository.save(clientMapper.dtoToEntity(request));
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public Page<ClientDtoResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public ClientDtoResponse getById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public ClientDtoResponse updateById(Long id, ClientDtoUpdateRequest request) {
        return null;
    }

    @Override
    @Transactional
    public ClientDtoResponse deleteById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public ClientDtoResponse getByPhone(String phone) {
        return null;
    }

    @Override
    @Transactional
    public Page<ClientDtoResponse> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public Page<ClientDtoResponse> findByLastVisitDateBetween(LocalDate lastVisitDateAfter, LocalDate lastVisitDateBefore, Pageable pageable) {
        return null;
    }
}
