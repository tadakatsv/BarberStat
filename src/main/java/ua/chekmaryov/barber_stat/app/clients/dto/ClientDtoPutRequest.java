package ua.chekmaryov.barber_stat.app.clients.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ua.chekmaryov.barber_stat.app.clients.domain.ClientStatus;

import java.time.LocalDate;

@Builder
public record ClientDtoPutRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{9,12}$", message = "Invalid phone format")
        String phone,
        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date can't be in the future")
        LocalDate birthDate,
        @NotNull(message = "Status is required")
        ClientStatus status,
        @Past(message = "Last visit date can't be in the past")
        LocalDate lastVisitDate,
        String notes
) {
}
