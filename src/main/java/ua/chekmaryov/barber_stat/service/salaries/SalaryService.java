package ua.chekmaryov.barber_stat.service.salaries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoResponse;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoUpdateRequest;

import java.time.LocalDateTime;

public interface SalaryService {
    Page<SalaryDtoResponse> getAll(Pageable pageable);
    SalaryDtoResponse getById(Long id);
    SalaryDtoResponse updateById(Long id, SalaryDtoUpdateRequest request);
    Page<SalaryDtoResponse> findByBarber_Id(Long barberId, Pageable pageable);
    SalaryDtoResponse checkSumSalaryForBarber(Long barberId, LocalDateTime newStart, LocalDateTime newEnd);
    SalaryDtoResponse saveSumSalaryForBarber(Long barberId, LocalDateTime newStart, LocalDateTime newEnd);
}
