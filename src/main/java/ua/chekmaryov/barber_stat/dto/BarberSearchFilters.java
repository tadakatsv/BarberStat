package ua.chekmaryov.barber_stat.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

public record BarberSearchFilters(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotNull BarberStatus status
) {
}
