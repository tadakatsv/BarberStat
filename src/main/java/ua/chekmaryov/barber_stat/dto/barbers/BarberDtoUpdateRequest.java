package ua.chekmaryov.barber_stat.dto.barbers;

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
        Integer salaryPercent,
        String notes
) {}