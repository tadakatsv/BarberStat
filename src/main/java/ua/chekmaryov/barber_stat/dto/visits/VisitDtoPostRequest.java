package ua.chekmaryov.barber_stat.dto.visits;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VisitDtoPostRequest(
        @NotNull(message = "Client id cannot be null")
        Long clientId,
        @NotNull(message = "Barber id cannot be null")
        Long barberId,
        @NotNull(message = "Offer id cannot be null")
        Long offerId,
        @NotNull(message = "Visit time cannot be null")
        @Future(message = "You cannot book a visit in the past")
        LocalDateTime visitTime,
        BigDecimal actualPrice,
        Integer actualBarberPercentage,
        VisitStatus status,
        Integer durationMinutes,
        String notes
)
{}