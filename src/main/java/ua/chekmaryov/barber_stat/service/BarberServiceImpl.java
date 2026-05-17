package ua.chekmaryov.barber_stat.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
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
public class BarberServiceImpl implements BarberService{

    private final BarberRepository barberRepository;
    private final BarberMapper barberMapper;

    public BarberServiceImpl(
                               BarberRepository barberRepository,
                               BarberMapper barberMapper){
        this.barberRepository=barberRepository;
        this.barberMapper=barberMapper;
    }

    @Override
    @Transactional
    public BarberDtoResponse create(BarberDtoCreateRequest request) {
        log.info("Attempting to create a new barber:{} {}",request.firstName(),request.lastName());
        if (barberRepository.existsByPhone(request.phone().replaceAll("\\s+",""))){
            throw new AlreadyExistsException("Barber with " + request.phone() +" already exists");
        }
        Barber barber = barberRepository.save(barberMapper.dtoToEntity(request));
        log.debug("Barber successfully saved with ID: {}", barber.getId());

        return barberMapper.toResponse(barber);
    }

    @Override
    @Transactional
    public Page<BarberDtoResponse> getAll(Pageable pageable) {
        log.info("Request to fetch all barbers");
        Page<Barber> allBarbers = barberRepository.findAll(pageable);
        log.debug("Retrieved {} records from database",allBarbers.getTotalElements());
        return allBarbers
                .map(barberMapper::toResponse);
    }

    @Override
    @Transactional
    public BarberDtoResponse getById(Long id) {
        log.info("Searching for barber with id: {}" ,id);
        Barber barber = barberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), id);
        return barberMapper.toResponse(barber);
    }

    @Override
    @Transactional
    public BarberDtoResponse updateById(Long id, BarberDtoUpdateRequest request) {
        log.info("Updating barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        log.debug("Retrieved barber:{} {}", barber.getFirstName(), barber.getLastName());
        if (!Objects.equals(barber.getPhone(), request.phone().replaceAll("\\s+",""))){
            if(barberRepository.existsByPhone(request.phone().replaceAll("\\s+",""))){
                throw new AlreadyExistsException("Barber with " + request.phone() +" already exists");
            }
        }
        Barber updated = barberRepository.save(barberMapper.dtoUpdateToEntity(request,barber));
        log.debug("Barber ID {} successfully updated", id);
        return barberMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public BarberDtoResponse deleteById(Long id) {
        log.info("Attempting to soft delete barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        barber.setStatus(BarberStatus.FIRED);
        log.debug("Barber ID {} status changed to FIRED", id);
        return barberMapper.toResponse(barber);
    }

    @Override
    @Transactional
    public Page<BarberDtoResponse> findByFirstNameAndLastName(String firstName, String lastName,Pageable pageable) {
        log.info("Searching for barbers with filter: firstName={}, lastName={}", firstName, lastName);

        Page<BarberDtoResponse> foundBarbers = barberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName,lastName,pageable)
                .map(barberMapper::toResponse);
        log.debug("Search complete. Found {} barbers matching the criteria", foundBarbers.getTotalElements());
        return foundBarbers;
    }

    @Override
    @Transactional
    public Page<BarberDtoResponse> findByStatus(BarberStatus status,Pageable pageable) {
        log.info("Fetching barbers with status: {}", status);
        Page<BarberDtoResponse> foundBarbers = barberRepository.findBarbersByStatusIs(status, pageable)
                .map(barberMapper::toResponse);
        log.debug("Search complete. Found {} barbers matching the criteria", foundBarbers.getTotalElements());
        return foundBarbers;
    }
}
