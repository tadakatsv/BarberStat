package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.BarberOffering;
import ua.chekmaryov.barber_stat.entity.Offer;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BarberOfferingRepository extends JpaRepository<BarberOffering,Long> {
    Optional<BarberOffering> findByBarberIdAndOfferId(Long barberId, Long offerId);
}
