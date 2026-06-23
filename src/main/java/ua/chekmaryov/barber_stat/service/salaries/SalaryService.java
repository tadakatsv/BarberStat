package ua.chekmaryov.barber_stat.service.salaries;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoResponse;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoUpdateRequest;
import ua.chekmaryov.barber_stat.persistence.entity.Barber;
import ua.chekmaryov.barber_stat.persistence.entity.Salary;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.SalaryMapper;
import ua.chekmaryov.barber_stat.persistence.repository.BarberRepository;
import ua.chekmaryov.barber_stat.persistence.repository.SalaryRepository;
import ua.chekmaryov.barber_stat.persistence.repository.VisitRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryMapper mapper;
    private final SalaryRepository salaryRepository;
    private final VisitRepository visitRepository;
    private final BarberRepository barberRepository;

    public Page<SalaryDtoResponse> getAll(Pageable pageable) {
        Page<Salary> allSalaries = salaryRepository.findAll(pageable);
        return allSalaries.map(mapper::toResponse);
    }

    public SalaryDtoResponse getById(Long id) {
        Salary neededSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No salary record by this id:" + id));
        return mapper.toResponse(neededSalary);
    }


    @Transactional
    public SalaryDtoResponse updateById(Long id, SalaryDtoUpdateRequest request) {
        Salary neededSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No salary record by this id:" + id));
        Salary updated = salaryRepository.save(mapper.dtoUpdateToEntity(request, neededSalary));
        return mapper.toResponse(updated);
    }


    public SalaryDtoResponse checkSumSalaryForBarber(Long barberId, LocalDateTime newStart, LocalDateTime newEnd) {
        if (newStart.isAfter(newEnd)) {
            throw new BadRequestException("Start date (" + newStart + ") cannot be after end date (" + newEnd + ")");
        }
        log.info("Searching for barber with id: {}", barberId);
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), barberId);
        BigDecimal sumSalary = visitRepository.sumSalaryForBarber(barberId, newStart, newEnd)
                .orElse(BigDecimal.ZERO);
        Salary salary = mapper.dtoToEntity(newStart, newEnd, barber, sumSalary);
        return mapper.toResponse(salary);
    }


    public SalaryDtoResponse saveSumSalaryForBarber(Long barberId, LocalDateTime newStart, LocalDateTime newEnd) {
        if (newStart.isAfter(newEnd)) {
            throw new BadRequestException("Start date (" + newStart + ") cannot be after end date (" + newEnd + ")");
        }
        log.info("Searching for barber with id: {}", barberId);
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), barberId);
        BigDecimal sumSalary = visitRepository.sumSalaryForBarber(barberId, newStart, newEnd)
                .orElse(BigDecimal.ZERO);
        Salary salary = salaryRepository.save(mapper.dtoToEntity(newStart, newEnd, barber, sumSalary));
        return mapper.toResponse(salary);
    }
}
