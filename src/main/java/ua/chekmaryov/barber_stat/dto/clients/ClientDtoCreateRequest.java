package ua.chekmaryov.barber_stat.dto.clients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

public record ClientDtoCreateRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Phone number is required") String phone,
        @NotNull(message = "Birth date is required") LocalDate birthDate,
        ClientStatus status,
        LocalDate lastVisitDate,
        String notes
) {
}