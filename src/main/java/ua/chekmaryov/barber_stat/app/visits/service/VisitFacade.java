package ua.chekmaryov.barber_stat.app.visits.service;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoPatchRequest;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoPostRequest;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoPutRequest;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoResponse;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitSearchFilters;
import ua.chekmaryov.barber_stat.app.visits.VisitMapper;
import ua.chekmaryov.barber_stat.app.barbers.persistence.Barber;
import ua.chekmaryov.barber_stat.app.barberofferings.persistence.BarberOffering;
import ua.chekmaryov.barber_stat.app.clients.persistence.Client;
import ua.chekmaryov.barber_stat.app.offers.persistence.Offer;
import ua.chekmaryov.barber_stat.app.visits.persistence.Visit;
import ua.chekmaryov.barber_stat.app.barberofferings.service.BarberOfferingService;
import ua.chekmaryov.barber_stat.app.barbers.service.BarberService;
import ua.chekmaryov.barber_stat.app.clients.service.ClientService;
import ua.chekmaryov.barber_stat.app.offers.service.OfferService;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class VisitFacade {
    private final BarberOfferingService barberOfferingService;
    private final ClientService clientService;
    private final BarberService barberService;
    private final OfferService offerService;
    private final VisitService visitService;
    private final VisitMapper visitMapper;

    public VisitDtoResponse create(VisitDtoPostRequest request) {
        Barber barber = barberService.getById(request.barberId());
        Offer offer = offerService.getById(request.offerId());
        Client client = clientService.getById(request.clientId());
        BarberOffering barberOffering = barberOfferingService.findByBarberIdAndOfferId(request.barberId(), request.offerId());
        Visit visit = visitService.create(request, barber, offer, client, barberOffering);
        return visitMapper.toResponse(visit);
    }

    public Page<VisitDtoResponse> getAll(Pageable pageable, VisitSearchFilters f) {
        Specification<Visit> spec = Specification.where((_, _, cb) -> cb.conjunction());

        spec = buildEqual(spec, r -> r.join("client").get("id"), f.clientId());
        spec = buildEqual(spec, r -> r.join("barber").get("id"), f.barberId());
        spec = buildEqual(spec, r -> r.join("offer").get("id"), f.offerId());
        spec = buildEqual(spec, r -> r.get("visitTime"), f.visitTime());
        spec = buildEqual(spec, r -> r.get("actualPrice"), f.actualPrice());
        spec = buildEqual(spec, r -> r.get("actualBarberPercentage"), f.actualBarberPercentage());
        spec = buildEqual(spec, r -> r.get("status"), f.status());
        spec = buildEqual(spec, r -> r.get("durationMinutes"), f.durationMinutes());
        spec = buildBetween(spec, r -> r.get("visitTime"),   f.visitTimeStart(), f.visitTimeEnd());
        spec = buildBetween(spec, r -> r.get("actualPrice"), f.actualPriceStart(), f.actualPriceEnd());

        return visitService.getAll(spec, pageable)
                .map(visitMapper::toResponse);
    }

    private <T, S> Specification<S> buildEqual(Specification<S> spec,
                                               Function<Root<S>, Path<?>> pathExtractor,
                                               T value) {

        // If the filter parameter wasn't passed, return the Spec untouched
        if (value == null) {
            return spec;
        }

        return spec.and((root, _, cb) -> cb.equal(
                pathExtractor.apply(root), value
        ));
    }

    private <S, Y extends Comparable<? super Y>> Specification<S> buildBetween(
            Specification<S> spec,
            Function<Root<S>, Path<?>> pathExtractor,
            Y start,
            Y end) {

        if (start == null && end == null) {
            return spec;
        }

        return spec.and((root, query, cb) -> {
            // 1. Extract the raw path (e.g. Path<Object>)
            Path<?> rawPath = pathExtractor.apply(root);

            // 2. Force-cast it to the Comparable type the CriteriaBuilder demands.
            // At runtime, Generics are erased anyway, so the JVM will allow this.
            @SuppressWarnings("unchecked")
            Expression<Y> expression = (Expression<Y>) rawPath;

            if (start != null && end != null) {
                if (start.compareTo(end) > 0) {
                    throw new IllegalArgumentException("Filter 'start' cannot be strictly greater than 'end'");
                }
                return cb.between(expression, start, end);
            } else if (start != null) {
                return cb.greaterThanOrEqualTo(expression, start);
            } else {
                return cb.lessThanOrEqualTo(expression, end);
            }
        });
    }

    public VisitDtoResponse getById(Long id) {
        Visit visit = visitService.getById(id);
        return visitMapper.toResponse(visit);
    }

    public VisitDtoResponse patchById(Long id, VisitDtoPatchRequest request) {
        Visit visit = visitService.patchById(id, request);
        return visitMapper.toResponse(visit);
    }

    public VisitDtoResponse updateById(Long id, VisitDtoPutRequest request) {
        Visit visit = visitService.updateById(id, request);
        return visitMapper.toResponse(visit);
    }
}
