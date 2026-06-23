package ua.chekmaryov.barber_stat.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.persistence.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long>, JpaSpecificationExecutor<Barber> {

    /*
     * how it should be like
     * SELECT *
       FROM barbers b
       WHERE (:barber_status is null or b.status = :barber_status)
            AND (:barber_role is null or b.barber_role = :barber_role)
            AND (:first_name is null or b.first_name = :first_name)
            AND (:last_name is null or b.last_name = :last_name)
     */
    @Query(value = """
            SELECT b FROM Barber b
                   WHERE (:barberStatus is null or b.status = :barberStatus)
                        AND (:barberRole is null or b.role = :barberRole)
                        AND (:firstName is null or b.firstName = :firstName)
                        AND (:lastName is null or b.lastName = :lastName)
            
            """
    )
    Page<Barber> findAll(Pageable pageable,
                         @Param("firstName") String firstName,
                         @Param("lastName") String lastName,
                         @Param("barberStatus") BarberStatus barberStatus,
                         @Param("barberRole") BarberRole barberRole
    );

    boolean existsByPhone(String phone);
}
