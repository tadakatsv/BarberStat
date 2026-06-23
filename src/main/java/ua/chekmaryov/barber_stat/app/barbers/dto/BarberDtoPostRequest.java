package ua.chekmaryov.barber_stat.app.barbers.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import ua.chekmaryov.barber_stat.app.barbers.domain.BarberRole;
import ua.chekmaryov.barber_stat.app.barbers.domain.BarberStatus;

import java.time.LocalDate;

@Builder
public record BarberDtoPostRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{9,12}$", message = "Invalid phone format")
        String phone,
        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date can't be in the future")
        LocalDate birthDate,
        BarberStatus status,
        BarberRole role,
        @Positive(message = "Salary percent cannot be less than 0")
        @Max(value = 100, message = "Salary percent cannot be more than 100")
        Integer salaryPercent,
        String notes
) {
}