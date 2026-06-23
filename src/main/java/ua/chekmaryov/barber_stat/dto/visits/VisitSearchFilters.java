package ua.chekmaryov.barber_stat.dto.visits;

import ua.chekmaryov.barber_stat.enums.VisitStatus;

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
