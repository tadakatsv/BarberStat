package ua.chekmaryov.barber_stat.app.barberofferings.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarberOfferingRepository extends JpaRepository<BarberOffering, Long>, JpaSpecificationExecutor<BarberOffering> {
    Optional<BarberOffering> findByBarberIdAndOfferId(Long barberId, Long offerId);

    //делать ли в service эту тему
    //для проверки перед тем как добавлять в таблицу
    boolean existsByBarber_IdAndOffer_Id(Long barberId, Long offerId);
}
