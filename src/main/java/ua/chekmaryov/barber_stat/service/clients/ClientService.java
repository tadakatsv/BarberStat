package ua.chekmaryov.barber_stat.service.clients;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

public interface ClientService {
    ClientDtoResponse create(ClientDtoCreateRequest request);
    Page<ClientDtoResponse> getAll(Pageable pageable);
    ClientDtoResponse getById(Long id);
    ClientDtoResponse updateById(Long id, ClientDtoUpdateRequest request);
    ClientDtoResponse deleteById(Long id);
    ClientDtoResponse getByPhone(String phone);
    Page<ClientDtoResponse> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
    Page<ClientDtoResponse> findByStatusAndLastVisitDateBetween(ClientStatus status, LocalDate lastVisitDateAfter, LocalDate lastVisitDateBefore, Pageable pageable);
}
