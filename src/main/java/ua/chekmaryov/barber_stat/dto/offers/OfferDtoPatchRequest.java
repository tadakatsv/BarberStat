package ua.chekmaryov.barber_stat.dto.offers;

import jakarta.validation.constraints.NotBlank;

public record OfferDtoPatchRequest(
        @NotBlank(message = "Name is required") String name
) {
}
