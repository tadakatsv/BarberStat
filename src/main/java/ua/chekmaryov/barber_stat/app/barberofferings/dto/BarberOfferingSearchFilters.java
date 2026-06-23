package ua.chekmaryov.barber_stat.app.barberofferings.dto;

import java.math.BigDecimal;

public record BarberOfferingSearchFilters(
        Long barberId,
        Long offerId,
        BigDecimal price,
        Integer customTime
) {
}
