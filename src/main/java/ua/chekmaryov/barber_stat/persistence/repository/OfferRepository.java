package ua.chekmaryov.barber_stat.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.persistence.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long>, JpaSpecificationExecutor<Offer> {

    boolean existsOfferByName(String name);

}
