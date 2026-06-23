package ua.chekmaryov.barber_stat.dto.clients;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;

@Builder
public record ClientDtoPatchRequest(
        String firstName,
        String lastName,
        @Pattern(regexp = "^\\d{9,12}$", message = "Invalid phone format")
        String phone,
        @Past(message = "Birth date can't be in the future")
        LocalDate birthDate,
        ClientStatus status,
        @Past(message = "Last visit date can't be in the future")
        LocalDate lastVisitDate,
        String notes
) {
}
