package ua.chekmaryov.barber_stat.dto.clients;

import jakarta.validation.constraints.*;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

@Builder
public record ClientDtoCreateRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{9,12}$", message = "Invalid phone format")
        String phone,
        @NotNull(message = "Birth date is required")
        LocalDate birthDate,
        ClientStatus status,
        @Past(message = "Last visit date can't be in the past")
        LocalDate lastVisitDate,
        String notes
) {
}