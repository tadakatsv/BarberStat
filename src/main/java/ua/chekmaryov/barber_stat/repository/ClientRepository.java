package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Client;
import ua.chekmaryov.barber_stat.enums.ClientStatus;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientByPhone(String phone);

    Page<Client> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    Page<Client> findClientsByStatusAndLastVisitDateBetween(ClientStatus status,LocalDate lastVisitDateAfter, LocalDate lastVisitDateBefore, Pageable pageable);

    boolean existsByPhone(String phone);

    //добавить может update по статусу
}
