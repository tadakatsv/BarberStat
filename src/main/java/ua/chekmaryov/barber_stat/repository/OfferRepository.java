package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {
    Page<Offer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsOfferByName(String name);

}
