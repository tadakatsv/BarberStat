package ua.chekmaryov.barber_stat.service.barbers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPatchRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPutRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.BarberMapper;
import ua.chekmaryov.barber_stat.repository.BarberRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Objects;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;
    private final BarberMapper barberMapper;

    @Transactional
    public Barber create(BarberDtoPostRequest request) {
        log.info("Attempting to create a new barber:{} {}", request.firstName(), request.lastName());
        if (barberRepository.existsByPhone(request.phone().replaceAll("\\s+", ""))) {
            throw new AlreadyExistsException("Barber with " + request.phone() + " already exists");
        }
        Barber barber = barberRepository.save(barberMapper.dtoToEntity(request));
        log.debug("Barber successfully saved with ID: {}", barber.getId());

        return barber;
    }

    public Page<Barber> getAll(Pageable pageable, String firstName, String lastName, BarberStatus status, BarberRole role) {
        log.info("Request to fetch all barbers");
        Page<Barber> allBarbers = barberRepository.findAll(pageable, firstName, lastName, status, role);
        log.debug("Retrieved {} records from database", allBarbers.getTotalElements());
        return allBarbers;
    }

    public Page<Barber> searchAll(Pageable pageable, Specification<Barber> spec) {
        log.info("Request to fetch all barbers");
        Page<Barber> allBarbers = barberRepository.findAll(spec, pageable);
        log.debug("Retrieved {} records from database", allBarbers.getTotalElements());
        return allBarbers;
    }

    public Barber getById(Long id) {
        log.info("Searching for barber with id: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), id);
        return barber;
    }

    @Transactional
    public Barber patchById(Long id, BarberDtoPatchRequest request) {
        log.info("Patching barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        log.debug("Retrieved barber:{} {}", barber.getFirstName(), barber.getLastName());
        if (request.phone() != null) {
            if (!Objects.equals(barber.getPhone(), request.phone().replaceAll("\\s+", ""))) {
                if (barberRepository.existsByPhone(request.phone().replaceAll("\\s+", ""))) {
                    throw new AlreadyExistsException("Barber from request with " + request.phone() + " already exists");
                }
            }
        }
        Barber updated = barberRepository.save(barberMapper.dtoToEntity(request, barber));
        log.debug("Barber ID {} successfully patched", id);
        return updated;
    }

    @Transactional
    public Barber updateById(Long id, BarberDtoPutRequest request) {
        log.info("Patching barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        log.debug("Retrieved barber:{} {}", barber.getFirstName(), barber.getLastName());
        if (request.phone() != null) {
            if (!Objects.equals(barber.getPhone(), request.phone().replaceAll("\\s+", ""))) {
                if (barberRepository.existsByPhone(request.phone().replaceAll("\\s+", ""))) {
                    throw new AlreadyExistsException("Barber from request with " + request.phone() + " already exists");
                }
            }
        }
        Barber updated = barberRepository.save(barberMapper.dtoToEntity(id, request));
        log.debug("Barber ID {} successfully updated by put", id);
        return updated;
    }
}
