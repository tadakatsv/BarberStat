package ua.chekmaryov.barber_stat.dto.barbers;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

import java.time.LocalDate;

@Builder
public record BarberDtoCreateRequest(
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @NotBlank(message = "Phone number is required") String phone,
    @NotNull(message = "Birth date is required") LocalDate birthDate,
    BarberStatus status,
    BarberRole role,
    @Min(value = 0, message = "Salary percent cannot be less than 0")
    @Max(value = 100, message = "Salary percent cannot be more than 100")
    Integer salaryPercent,
    String notes
    ){
}