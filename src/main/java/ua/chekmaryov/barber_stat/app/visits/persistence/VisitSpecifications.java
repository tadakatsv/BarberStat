package ua.chekmaryov.barber_stat.app.visits.persistence;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import ua.chekmaryov.barber_stat.app.barbers.persistence.Barber_;
import ua.chekmaryov.barber_stat.app.clients.persistence.Client_;
import ua.chekmaryov.barber_stat.app.offers.persistence.Offer_;
import ua.chekmaryov.barber_stat.app.visits.domain.VisitStatus;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitSearchFilters;
import ua.chekmaryov.barber_stat.util.SpecificationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class VisitSpecifications {

    private VisitSpecifications() {}

    public static Specification<Visit> bySearchFilters(VisitSearchFilters f) {
        return Specification.where(hasClientId(f.clientId()))
                .and(hasBarberId(f.barberId()))
                .and(hasOfferId(f.offerId()))
                .and(hasStatus(f.status()))
                .and(visitTimeBetween(f.visitTimeStart(), f.visitTimeEnd()))
                .and(priceBetween(f.actualPriceStart(), f.actualPriceEnd()));
    }

    private static Specification<Visit> hasClientId(Long clientId) {
        return SpecificationUtil.equals(r -> r.join(Visit_.client, JoinType.LEFT).get(Client_.id), clientId);
    }

    private static Specification<Visit> hasBarberId(Long barberId) {
        return SpecificationUtil.equals(r -> r.join(Visit_.barber, JoinType.LEFT).get(Barber_.id), barberId);
    }

    private static Specification<Visit> hasOfferId(Long offerId) {
        return SpecificationUtil.equals(r -> r.join(Visit_.offer, JoinType.LEFT).get(Offer_.id), offerId);
    }

    private static Specification<Visit> hasStatus(VisitStatus status) {
        return SpecificationUtil.equals(r -> r.get(Visit_.status), status);
    }

    private static Specification<Visit> visitTimeBetween(LocalDateTime start, LocalDateTime end) {
        return SpecificationUtil.between(r -> r.get(Visit_.visitTime), start, end);
    }

    private static Specification<Visit> priceBetween(BigDecimal start, BigDecimal end) {
        return SpecificationUtil.between(r -> r.get(Visit_.actualPrice), start, end);
    }
}