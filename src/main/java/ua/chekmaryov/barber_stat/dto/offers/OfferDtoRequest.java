package ua.chekmaryov.barber_stat.dto.offers;

import jakarta.validation.constraints.NotBlank;

public record OfferDtoRequest(
        @NotBlank(message = "Name is required") String name
){}
