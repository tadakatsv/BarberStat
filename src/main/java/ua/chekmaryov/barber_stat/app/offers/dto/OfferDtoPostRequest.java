package ua.chekmaryov.barber_stat.app.offers.dto;

import jakarta.validation.constraints.NotBlank;

public record OfferDtoPostRequest(
        @NotBlank(message = "Name is required") String name
){}
