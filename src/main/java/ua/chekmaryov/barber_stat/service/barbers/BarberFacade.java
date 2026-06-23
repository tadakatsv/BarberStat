package ua.chekmaryov.barber_stat.service.barbers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.barbers.BarberSearchFilters;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPutRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPatchRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.mapper.BarberMapper;

@Service
@RequiredArgsConstructor
public class BarberFacade {

    private final BarberMapper barberMapper;
    private final BarberService barberService;

    public BarberDtoResponse create(BarberDtoPostRequest request) {
        Barber barber = barberService.create(request);
        return barberMapper.toResponse(barber);
    }

    public Page<BarberDtoResponse> getAll(Pageable pageable, BarberSearchFilters barberSearchFilters) {
        String firstName = barberSearchFilters.firstName();
        String lastName = barberSearchFilters.lastName();
        BarberStatus status = barberSearchFilters.status();
        BarberRole role = barberSearchFilters.role();
        return barberService.getAll(pageable, firstName, lastName, status, role)
                .map(barberMapper::toResponse);
    }

    public Page<BarberDtoResponse> searchAll(Pageable pageable, BarberSearchFilters barberSearchFilters) {
        Specification<Barber> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (barberSearchFilters.firstName() != null && !barberSearchFilters.firstName().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("firstName")),
                    "%" + barberSearchFilters.firstName().trim().toLowerCase() + "%"
            ));
        }

        if (barberSearchFilters.lastName() != null && !barberSearchFilters.lastName().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("lastName")),
                    "%" + barberSearchFilters.lastName().trim().toLowerCase() + "%"
            ));
        }

        if (barberSearchFilters.phone() != null && !barberSearchFilters.phone().isBlank()) {
            String cleanPhone = barberSearchFilters.phone().replaceAll("\\s+", "");
            spec = spec.and((root, query, cb) -> cb.like(
                    root.get("phone"), "%" + cleanPhone + "%"));
        }

        if (barberSearchFilters.status() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("status"), barberSearchFilters.status()));
        }

        if (barberSearchFilters.role() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("role"), barberSearchFilters.role()));
        }


        if (barberSearchFilters.birthDate() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("birthDate"), barberSearchFilters.birthDate()));
        }

        if (barberSearchFilters.salaryPercent() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("salaryPercent"), barberSearchFilters.salaryPercent()));
        }

        return barberService.searchAll(pageable, spec)
                .map(barberMapper::toResponse);
    }

    public BarberDtoResponse getById(Long id) {
        return barberMapper.toResponse(barberService.getById(id));//Делать так, или как Толик в create
    }

    public BarberDtoResponse patchBarberById(Long id, BarberDtoPatchRequest request) {
        return barberMapper.toResponse(barberService.patchById(id, request));
    }

    public BarberDtoResponse updateBarberById(Long id, BarberDtoPutRequest request) {
        return barberMapper.toResponse(barberService.updateById(id, request));
    }
}
