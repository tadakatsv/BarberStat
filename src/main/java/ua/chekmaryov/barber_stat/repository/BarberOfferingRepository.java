package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.BarberOffering;

import java.util.Optional;

@Repository
public interface BarberOfferingRepository extends JpaRepository<BarberOffering,Long> {
    Optional<BarberOffering> findByBarberIdAndOfferId(Long barberId, Long offerId);

    //для проверки перед тем как добавлять в таблицу
    boolean existsByBarber_IdAndOffer_Id(Long barberId, Long offerId);
}
