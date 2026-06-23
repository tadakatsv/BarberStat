package ua.chekmaryov.barber_stat.dto.barberOffering;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BarberOfferingDtoPatchRequest(
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,
        @Positive(message = "Duration must be greater than zero")
        Integer customTime
) {
}
