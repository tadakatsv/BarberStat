package ua.chekmaryov.barber_stat.service.salaries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoResponse;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.entity.Salary;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.SalaryMapper;
import ua.chekmaryov.barber_stat.repository.BarberRepository;
import ua.chekmaryov.barber_stat.repository.SalaryRepository;
import ua.chekmaryov.barber_stat.repository.VisitRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SalaryServiceImpl implements SalaryService {
    private final SalaryMapper mapper;
    private final SalaryRepository salaryRepository;
    private final VisitRepository visitRepository;
    private final BarberRepository barberRepository;

    public SalaryServiceImpl(SalaryMapper mapper, SalaryRepository salaryRepository, VisitRepository visitRepository, BarberRepository barberRepository) {
        this.mapper = mapper;
        this.salaryRepository = salaryRepository;
        this.visitRepository = visitRepository;
        this.barberRepository = barberRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryDtoResponse> getAll(Pageable pageable) {
        Page<Salary> allSalaries = salaryRepository.findAll(pageable);
        return allSalaries.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryDtoResponse getById(Long id) {
        Salary neededSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No salary record by this id:" + id));
        return mapper.toResponse(neededSalary);
    }

    @Override
    @Transactional
    public SalaryDtoResponse updateById(Long id, SalaryDtoUpdateRequest request) {
        Salary neededSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No salary record by this id:" + id));
        Salary updated = salaryRepository.save(mapper.dtoUpdateToEntity(request,neededSalary));
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryDtoResponse> findByBarber_Id(Long barberId, Pageable pageable) {
        log.info("Searching for barber with id: {}" ,barberId);
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), barberId);
        Page<Salary> neededSalaries = salaryRepository.findSalariesByBarber_Id(barberId, pageable);
        return neededSalaries.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryDtoResponse checkSumSalaryForBarber(Long barberId, LocalDateTime newStart, LocalDateTime newEnd) {
        if (newStart.isAfter(newEnd)){
            throw new BadRequestException("Start date (" + newStart + ") cannot be after end date (" + newEnd + ")");
        }
        log.info("Searching for barber with id: {}" ,barberId);
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), barberId);
        BigDecimal sumSalary = visitRepository.sumSalaryForBarber(barberId,newStart,newEnd)
                .orElse(BigDecimal.ZERO);
        Salary salary = mapper.dtoToEntity(newStart,newEnd,barber,sumSalary);
        return mapper.toResponse(salary);
    }

    @Override
    @Transactional
    public SalaryDtoResponse saveSumSalaryForBarber(Long barberId, LocalDateTime newStart, LocalDateTime newEnd) {
        if (newStart.isAfter(newEnd)){
            throw new BadRequestException("Start date (" + newStart + ") cannot be after end date (" + newEnd + ")");
        }
        log.info("Searching for barber with id: {}" ,barberId);
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), barberId);
        BigDecimal sumSalary = visitRepository.sumSalaryForBarber(barberId,newStart,newEnd)
                .orElse(BigDecimal.ZERO);
        Salary salary = salaryRepository.save(mapper.dtoToEntity(newStart,newEnd,barber,sumSalary));
        return mapper.toResponse(salary);
    }
}
