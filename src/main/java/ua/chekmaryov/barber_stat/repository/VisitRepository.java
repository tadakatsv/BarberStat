package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Visit;

import java.time.Instant;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findVisitByClientId(Long clientId);

    List<Visit> findByClientIdAndVisitTimeBetween(Long clientId, Instant start, Instant end);

    List<Visit> findByVisitTimeBetween(Instant start, Instant end);

    List<Visit> findByBarberIdAndVisitTimeBetween(Long barberId, Instant start, Instant end);

    boolean existsByBarberIdAndVisitTime(Long barberId, Instant visitTime);
}
