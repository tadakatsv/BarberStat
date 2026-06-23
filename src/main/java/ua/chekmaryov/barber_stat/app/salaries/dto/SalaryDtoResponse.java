package ua.chekmaryov.barber_stat.app.salaries.dto;

import lombok.Builder;
import ua.chekmaryov.barber_stat.app.salaries.domain.SalaryStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record SalaryDtoResponse(
        Long id,
        Long barberId,
        String barberFullName,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal totalSum,
        SalaryStatus status
) {
}
