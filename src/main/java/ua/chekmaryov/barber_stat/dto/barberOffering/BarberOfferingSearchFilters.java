package ua.chekmaryov.barber_stat.dto.barberOffering;

import java.math.BigDecimal;

public record BarberOfferingSearchFilters(
        Long barberId,
        Long offerId,
        BigDecimal price,
        Integer customTime
) {
}
