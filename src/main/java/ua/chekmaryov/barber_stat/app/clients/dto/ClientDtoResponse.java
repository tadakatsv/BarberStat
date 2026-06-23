package ua.chekmaryov.barber_stat.app.clients.dto;

import lombok.Builder;
import ua.chekmaryov.barber_stat.app.clients.domain.ClientStatus;

import java.time.LocalDate;

@Builder
public record ClientDtoResponse(
        Long id,
        String fullName,
        String phone,
        LocalDate birthDate,
        ClientStatus status,
        LocalDate lastVisitDate,
        String notes
) {
}
