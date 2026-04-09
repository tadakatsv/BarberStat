package ua.chekmaryov.barber_stat.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.ApiResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.BarberMapper;
import ua.chekmaryov.barber_stat.repository.BarberRepository;

import java.util.List;

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
    public ApiResponse<BarberDtoResponse> create(BarberDtoCreateRequest request) {
        log.info("Attempting to create a new barber:{} {}",request.firstName(),request.lastName());
        if (barberRepository.existsByPhone(request.phone())){
            throw new AlreadyExistsException("Barber with " + request.phone() +" already exists");
        }
        Barber barber = barberRepository.save(barberMapper.dtoToEntity(request));
        log.debug("Barber successfully saved with ID: {}", barber.getId());

        return ApiResponse.<BarberDtoResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(barberMapper.toResponse(barber))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<BarberDtoResponse>> getAll() {
        log.info("Request to fetch all barbers");

        List<BarberDtoResponse> allBarbers = barberMapper.toResponseList(barberRepository.findAll());
        log.debug("Retrieved {} records from database",allBarbers.size());

        return ApiResponse.<List<BarberDtoResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Barber list retrieved successfully")
                .data(allBarbers)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<BarberDtoResponse> getById(Long id) {
        log.info("Searching for barber with id: {}" ,id);
        Barber barber = barberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), id);
        return ApiResponse.<BarberDtoResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Barber found")
                .data(barberMapper.toResponse(barber))
                .build();
    }
    @Override
    @Transactional
    public ApiResponse<BarberDtoResponse> updateById(Long id, BarberDtoUpdateRequest request) {
        log.info("Updating barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        Barber updated = barberRepository.save(barberMapper.dtoUpdateToEntity(id,request,barber));
        log.debug("Barber ID {} successfully updated", id);
        return ApiResponse.<BarberDtoResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated barber by ID: " + id)
                .data(barberMapper.toResponse(updated))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<BarberDtoResponse> deleteById(Long id) {
        log.info("Attempting to soft delete barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        barber.setStatus(BarberStatus.FIRED);
        log.debug("Barber ID {} status changed to FIRED", id);
        return ApiResponse.<BarberDtoResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(barberMapper.toResponse(barber))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<BarberDtoResponse>> findByFirstNameAndLastName(String firstName, String lastName) {
        log.info("Searching for barbers with filter: firstName={}, lastName={}", firstName, lastName);

        List<BarberDtoResponse> foundBarbers = barberMapper.toResponseList(barberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName,lastName));
        log.debug("Search complete. Found {} barbers matching the criteria", foundBarbers.size());
        return ApiResponse.<List<BarberDtoResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(foundBarbers)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<BarberDtoResponse>> findByStatus(BarberStatus status) {
        log.info("Fetching barbers with status: {}", status);
        List<BarberDtoResponse> foundBarbers = barberMapper.toResponseList(barberRepository.findBarbersByStatusIs(status));
        return ApiResponse.<List<BarberDtoResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(foundBarbers)
                .build();
    }
}
