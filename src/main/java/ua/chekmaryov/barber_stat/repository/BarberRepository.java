package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.dto.BarberSearchFilters;
import ua.chekmaryov.barber_stat.entity.Barber;

@Repository
public interface BarberRepository extends JpaRepository<Barber,Long> {

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

            """
    )
    Page<Barber> findAll(Pageable pageable, BarberSearchFilters barberSearchFilters);

    boolean existsByPhone(String phone);
}
