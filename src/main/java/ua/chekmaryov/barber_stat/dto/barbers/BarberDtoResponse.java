package ua.chekmaryov.barber_stat.dto.barbers;

import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import java.time.LocalDate;

@Builder
public record BarberDtoResponse(
        Long id,
        String fullName,
        String phone,
        LocalDate birthDate,
        BarberStatus status,
        String notes
) {}