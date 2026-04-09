package ua.chekmaryov.barber_stat.dto.barbers;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

import java.time.LocalDate;

public record BarberDtoCreateRequest(
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @NotBlank(message = "Phone number is required") String phone,
    @NotNull(message = "Birth date is required") LocalDate birthDate,
    BarberStatus status,
    String notes
    ){
}
