package ua.chekmaryov.barber_stat.app.visits.dto;

import lombok.Builder;
import ua.chekmaryov.barber_stat.app.visits.domain.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VisitDtoResponse(
        Long id,
        Long barberId,
        String barberFullName,
        Long clientId,
        String clientFullName,
        Long offerId,
        String offerName,
        LocalDateTime visitTime,
        BigDecimal actualPrice,
        Integer actualBarberPercentage,
        VisitStatus status,
        Integer durationMinutes,
        String notes
) {
}
