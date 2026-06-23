package ua.chekmaryov.barber_stat.app.barberofferings.dto;

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
