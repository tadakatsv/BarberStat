package ua.chekmaryov.barber_stat.app.barberofferings.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.chekmaryov.barber_stat.app.barbers.persistence.Barber;
import ua.chekmaryov.barber_stat.app.offers.persistence.Offer;

import java.math.BigDecimal;

@Entity
@Table(name = "barber_offerings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "custom_time", nullable = false)
    private Integer customTime;
}