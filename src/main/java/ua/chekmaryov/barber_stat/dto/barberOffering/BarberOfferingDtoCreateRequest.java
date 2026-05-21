package ua.chekmaryov.barber_stat.dto.barberOffering;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BarberOfferingDtoCreateRequest(
        @NotNull(message = "Barber ID is required")
        Long barberId,

        @NotNull(message = "Offer ID is required")
        Long offerId,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be greater than zero")
        Integer durationMinutes
) {
}
