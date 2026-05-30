package ua.chekmaryov.barber_stat.dto.salaries;

import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.SalaryStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record SalaryDtoResponse(
        Long id,
        Long barberId,
        String fullNameBarber,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal totalSum,
        SalaryStatus status
) {
}
