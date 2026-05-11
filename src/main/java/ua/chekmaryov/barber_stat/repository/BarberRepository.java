package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

@Repository
public interface BarberRepository extends JpaRepository<Barber,Long> {
    Page<Barber> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    Page<Barber> findBarbersByStatusIs(BarberStatus status,Pageable pageable);

    boolean existsByPhone(String phone);
}
