package ua.chekmaryov.barber_stat.service.barbers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.BarberStatus;


public interface BarberService {
    BarberDtoResponse create(BarberDtoCreateRequest request);
    Page<BarberDtoResponse> getAll(Pageable pageable);
    BarberDtoResponse getById(Long id);
    BarberDtoResponse updateById(Long id, BarberDtoUpdateRequest request);
    BarberDtoResponse deleteById(Long id);
    Page<BarberDtoResponse> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
    Page<BarberDtoResponse> findByStatus(BarberStatus status,Pageable pageable);
}
