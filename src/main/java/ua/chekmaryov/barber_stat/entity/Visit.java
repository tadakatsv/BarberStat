package ua.chekmaryov.barber_stat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Связь с мастером
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    // Связь с типом услуги
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Offer offer;

    @Column(name = "visit_time")
    private Instant visitTime;

    @Column(name = "actual_price")
    private BigDecimal actualPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VisitStatus status = VisitStatus.PLANNED;

    @Column(name = "notes")
    private String notes;
}
