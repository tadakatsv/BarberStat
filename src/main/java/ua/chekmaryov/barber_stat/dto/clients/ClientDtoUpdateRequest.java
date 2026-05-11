package ua.chekmaryov.barber_stat.dto.clients;

import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

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
