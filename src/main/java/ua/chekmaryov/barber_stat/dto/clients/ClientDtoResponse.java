package ua.chekmaryov.barber_stat.dto.clients;

import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

@Builder
public record ClientDtoResponse(
        Long id,
        String fullName,
        String lastName,
        String phone,
        LocalDate birthDate,
        ClientStatus status,
        String notes
) {
}
