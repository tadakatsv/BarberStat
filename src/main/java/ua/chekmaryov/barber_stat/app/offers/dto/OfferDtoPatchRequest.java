package ua.chekmaryov.barber_stat.app.offers.dto;

import jakarta.validation.constraints.NotBlank;

public record OfferDtoPatchRequest(
        @NotBlank(message = "Name is required") String name
) {
}
