package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

import java.util.List;

@Repository
public interface BarberRepository extends JpaRepository<Barber,Long> {
    List<Barber> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Barber> findBarbersByStatusIs(BarberStatus status);

    boolean existsByPhone(String phone);
}
