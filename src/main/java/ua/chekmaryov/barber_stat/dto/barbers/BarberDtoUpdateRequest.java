package ua.chekmaryov.barber_stat.dto.barbers;

import ua.chekmaryov.barber_stat.enums.BarberStatus;
import java.time.LocalDate;

public record BarberDtoUpdateRequest(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        BarberStatus status,
        String notes
) {}