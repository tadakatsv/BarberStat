package ua.chekmaryov.barber_stat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barbers")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BarberStatus status = BarberStatus.ACTIVE;

    @Column(name = "barber_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private BarberRole role;

    @Column(name = "salary_percent",nullable = false)
    private Integer salaryPercent = 50;

    @Column(name = "notes")
    private String notes;
}
