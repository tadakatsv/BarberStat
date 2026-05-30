package ua.chekmaryov.barber_stat.dto.salaries;

import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.SalaryStatus;

import java.math.BigDecimal;

@Builder
public record SalaryDtoUpdateRequest(
        BigDecimal totalSum,
        SalaryStatus status
) {
}
