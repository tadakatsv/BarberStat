package ua.chekmaryov.barber_stat.app.clients.dto;

import ua.chekmaryov.barber_stat.app.clients.domain.ClientStatus;

import java.time.LocalDate;

public record ClientSearchFilters(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        ClientStatus status,
        LocalDate lastVisitDate
) {
}
