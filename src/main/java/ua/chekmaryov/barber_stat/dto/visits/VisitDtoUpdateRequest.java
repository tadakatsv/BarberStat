package ua.chekmaryov.barber_stat.dto.visits;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VisitDtoUpdateRequest(
        LocalDateTime visitTime,
        @Positive( message = "Price can't be lower than 0")
        BigDecimal actualPrice,
        @Positive( message = "Percent can't be lower than 0")
        @Max(value = 100, message = "Percent can't be higher than 100")
        Integer actualBarberPercentage,
        VisitStatus status,
        @Positive( message = "Duration can't be lower than 0")
        Integer durationMinutes,
        String notes
) {
}
