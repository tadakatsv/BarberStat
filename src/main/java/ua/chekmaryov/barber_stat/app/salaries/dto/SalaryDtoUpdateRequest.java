package ua.chekmaryov.barber_stat.app.salaries.dto;

import lombok.Builder;
import ua.chekmaryov.barber_stat.app.salaries.domain.SalaryStatus;

import java.math.BigDecimal;

@Builder
public record SalaryDtoUpdateRequest(
        BigDecimal totalSum,
        SalaryStatus status
) {
}
