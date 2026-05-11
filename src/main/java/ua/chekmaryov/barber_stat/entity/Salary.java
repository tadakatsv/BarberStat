package ua.chekmaryov.barber_stat.entity;

import jakarta.persistence.*;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.enums.SalaryStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "total_sum", nullable = false)
    private BigDecimal actualPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SalaryStatus status = SalaryStatus.PENDING;
}
