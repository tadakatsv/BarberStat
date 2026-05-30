package ua.chekmaryov.barber_stat.dto.barbers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import java.time.LocalDate;

@Builder
public record BarberDtoUpdateRequest(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        BarberStatus status,
        BarberRole role,
        @Positive(message = "Percent can't be lower than 0")
        @Max(value = 100, message = "Percent can't be higher than 100")
        Integer salaryPercent,
        String notes
) {}