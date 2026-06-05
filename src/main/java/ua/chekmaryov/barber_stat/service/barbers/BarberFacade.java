package ua.chekmaryov.barber_stat.service.barbers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.BarberSearchFilters;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.mapper.BarberMapper;

@Service
@RequiredArgsConstructor
public class BarberFacade {

    private final BarberMapper barberMapper;
    private final BarberService barberService;

    public BarberDtoResponse create(BarberDtoCreateRequest request) {
        Barber barber = barberService.create(request);
        return barberMapper.toResponse(barber);
    }

    public Page<BarberDtoResponse> getAll(Pageable pageable, BarberSearchFilters barberSearchFilters) {
        return barberService.getAll(pageable, barberSearchFilters)
                .map(barberMapper::toResponse);
    }

    public BarberDtoResponse getById(Long id) {
        return null;
    }

    public BarberDtoResponse updateById(Long id, BarberDtoUpdateRequest request) {
        return null;
    }

    public void deleteById(@Positive Long id) {
    }

    public Page<BarberDtoResponse> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        return null;
    }

    public Page<BarberDtoResponse> findByStatus(BarberStatus status, Pageable pageable) {
        return null;
    }
}
