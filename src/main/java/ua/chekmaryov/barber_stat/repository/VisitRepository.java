package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Visit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {
    @Query("""
                SELECT COUNT(v) > 0 FROM Visit v 
                WHERE v.barber.id = :barberId 
                  AND v.status != 'CANCELLED'
                  AND :newStart < (v.visitTime + v.durationMinutes minute) 
                  AND :newEnd > v.visitTime
            """)
    boolean hasOverlappingVisit(
            @Param("barberId") Long barberId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );

    @Query("""
                SELECT COUNT(v) > 0 FROM Visit v 
                WHERE v.barber.id = :barberId 
                  AND v.status != 'CANCELLED'
                  AND v.id != :visitId
                  AND :newStart < (v.visitTime + v.durationMinutes minute) 
                  AND :newEnd > v.visitTime
            """)
    boolean hasOverlappingVisitButForHimself(
            @Param("barberId") Long barberId,
            @Param("visitId") Long visitId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );


    @Query("""
                SELECT SUM(v.actualPrice * (v.actualBarberPercentage / 100.0)) FROM Visit v 
                WHERE v.barber.id = :barberId 
                  AND v.status = 'COMPLETED'
                  AND :newStart < v.visitTime 
                  AND :newEnd > v.visitTime
            """)
    Optional<BigDecimal> sumSalaryForBarber(
            @Param("barberId") Long barberId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );
}
