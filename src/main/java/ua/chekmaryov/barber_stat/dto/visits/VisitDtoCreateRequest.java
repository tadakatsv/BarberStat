package ua.chekmaryov.barber_stat.dto.visits;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VisitDtoCreateRequest(
        @NotNull(message = "Client id cannot be null")
        Long clientId,
        @NotNull(message = "Barber id cannot be null")
        Long barberId,
        @NotNull(message = "Offer id cannot be null")
        Long offerId,
        @NotNull(message = "Visit time cannot be null")
        LocalDateTime visitTime,
        BigDecimal actualPrice,
        Integer actualBarberPercentage,
        VisitStatus status,
        Integer durationMinutes,
        String notes
)
{}