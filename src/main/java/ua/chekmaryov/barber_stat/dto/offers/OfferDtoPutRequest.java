package ua.chekmaryov.barber_stat.dto.offers;

import jakarta.validation.constraints.NotBlank;

public record OfferDtoPutRequest(
        @NotBlank(message = "Name is required") String name
) {
}
