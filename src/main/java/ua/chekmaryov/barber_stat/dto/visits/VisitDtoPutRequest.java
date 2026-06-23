package ua.chekmaryov.barber_stat.dto.visits;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VisitDtoPutRequest(
        @NotNull(message = "Visit time can't be null")
        @Future(message = "You cannot book a visit in the past")
        LocalDateTime visitTime,
        @NotNull(message = "Price can't be null")
        @Positive( message = "Price can't be lower than 0")
        BigDecimal actualPrice,
        @NotNull(message = "Barber percentage can't be null")
        @Positive( message = "Percent can't be lower than 0")
        @Max(value = 100, message = "Percent can't be higher than 100")
        Integer actualBarberPercentage,
        @NotNull(message = "Visit status can't be null")
        VisitStatus status,
        @NotNull(message = "Duration minutes can't be null")
        @Positive( message = "Duration can't be lower than 0")
        Integer durationMinutes,
        String notes
) {
}
