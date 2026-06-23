package ua.chekmaryov.barber_stat.app.barberofferings.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BarberOfferingDtoResponse(
        Long id,
        Long  barberId,
        String barberFullName,
        Long offerId,
        String offerName,
        BigDecimal price,
        Integer customTime
) {
}
