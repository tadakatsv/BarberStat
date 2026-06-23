package ua.chekmaryov.barber_stat.app.barbers.dto;

import lombok.Builder;
import ua.chekmaryov.barber_stat.app.barbers.domain.BarberRole;
import ua.chekmaryov.barber_stat.app.barbers.domain.BarberStatus;

import java.time.LocalDate;

@Builder
public record BarberSearchFilters(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        BarberStatus status,
        BarberRole role,
        Integer salaryPercent
) {
}