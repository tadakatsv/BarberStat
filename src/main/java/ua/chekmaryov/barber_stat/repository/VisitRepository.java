package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Visit;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Page<Visit> findVisitsByStatusAndVisitTimeBetween(VisitStatus status, LocalDateTime visitTimeAfter, LocalDateTime visitTimeBefore, Pageable pageable);

    Page<Visit> findVisitsByClient_IdAndStatus(Long clientId, VisitStatus status, Pageable pageable);

    Page<Visit> findByClientIdAndVisitTimeBetween(Long clientId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Visit> findByVisitTimeBetween(LocalDateTime start, LocalDateTime end,Pageable pageable);

    Page<Visit> findByBarberIdAndVisitTimeBetween(Long barberId, LocalDateTime start, LocalDateTime end,Pageable pageable);

    //сделать ещё чтоб нельзя было на одно время с одним статусом
//    boolean existsByBarberIdAndVisitTime(Long barberId, LocalDateTime visitTime);
//
//    boolean existsByBarber_IdAndClient_IdAndStatus(Long barberId, Long clientId, VisitStatus status);

    //boolean existsByVisitTimeBetweenAndBarber_IdAndStatus(LocalDateTime visitTimeAfter, LocalDateTime visitTimeBefore, Long barberId, VisitStatus status);

    List<Visit> findByBarberIdAndVisitTimeBetween(Long barberId, Instant start, Instant end);

    boolean existsByBarberIdAndVisitTime(Long barberId, Instant visitTime);
}
