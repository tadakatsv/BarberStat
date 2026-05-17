package ua.chekmaryov.barber_stat.dto.clients;

import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

@Builder
public record ClientDtoUpdateRequest(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        ClientStatus status,
        LocalDate lastVisitDate,
        String notes
) {
}
