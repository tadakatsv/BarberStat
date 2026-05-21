package ua.chekmaryov.barber_stat.dto.barberOffering;

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
        Long customTime
) {
}
