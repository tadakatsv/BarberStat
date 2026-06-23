package ua.chekmaryov.barber_stat.app.barbers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ua.chekmaryov.barber_stat.app.barbers.domain.BarberRole;
import ua.chekmaryov.barber_stat.app.barbers.domain.BarberStatus;

import java.time.LocalDate;

@Builder
public record BarberDtoPatchRequest(
        String firstName,
        String lastName,
        @Pattern(regexp = "^\\d{9,12}$", message = "Invalid phone format")
        String phone,
        @Past(message = "Birth date can't be in the future")
        LocalDate birthDate,
        BarberStatus status,
        BarberRole role,
        @Positive(message = "Percent can't be lower than 0")
        @Max(value = 100, message = "Percent can't be higher than 100")
        Integer salaryPercent,
        String notes
) {
}