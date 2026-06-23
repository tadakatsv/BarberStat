package ua.chekmaryov.barber_stat.app.visits.dto;

import ua.chekmaryov.barber_stat.app.visits.domain.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VisitSearchFilters(
        Long clientId,
        Long barberId,
        Long offerId,
        LocalDateTime visitTime,
        LocalDateTime visitTimeStart,
        LocalDateTime visitTimeEnd,
        BigDecimal actualPrice,
        BigDecimal actualPriceStart,
        BigDecimal actualPriceEnd,
        Integer actualBarberPercentage,
        VisitStatus status,
        Integer durationMinutes
) {
}
