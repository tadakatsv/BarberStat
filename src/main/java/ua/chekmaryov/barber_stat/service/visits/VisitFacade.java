package ua.chekmaryov.barber_stat.service.visits;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.visits.*;
import ua.chekmaryov.barber_stat.entity.*;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.mapper.VisitMapper;
import ua.chekmaryov.barber_stat.service.barberofferings.BarberOfferingService;
import ua.chekmaryov.barber_stat.service.barbers.BarberService;
import ua.chekmaryov.barber_stat.service.clients.ClientService;
import ua.chekmaryov.barber_stat.service.offers.OfferService;

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

    public Page<VisitDtoResponse> getAll(Pageable pageable, VisitSearchFilters visitSearchFilters) {
        Specification<Visit> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (visitSearchFilters.clientId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.join("client").get("id"), visitSearchFilters.clientId()));
        }
        if (visitSearchFilters.barberId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.join("barber").get("id"), visitSearchFilters.barberId()));
        }
        if (visitSearchFilters.offerId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.join("offer").get("id"), visitSearchFilters.offerId()));
        }
        if (visitSearchFilters.visitTime() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("visitTime"), visitSearchFilters.visitTime()));
        }
        if (visitSearchFilters.visitTimeStart() != null && visitSearchFilters.visitTimeEnd() != null) {
            if (visitSearchFilters.visitTimeStart().isAfter(visitSearchFilters.visitTimeEnd())) {
                throw new BadRequestException("Start date (" + visitSearchFilters.visitTimeStart() + ") cannot be after end date (" + visitSearchFilters.visitTimeEnd() + ")");
            }
            spec = spec.and((root, query, cb) -> cb.between(
                    root.get("visitTime"), visitSearchFilters.visitTimeStart(), visitSearchFilters.visitTimeEnd()));
        }
        if (visitSearchFilters.actualPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("actualPrice"), visitSearchFilters.actualPrice()));
        }
        if (visitSearchFilters.actualPriceStart() != null && visitSearchFilters.actualPriceEnd() != null) {
            spec = spec.and((root, query, cb) -> cb.between(
                    root.get("actualPrice"), visitSearchFilters.actualPriceStart(), visitSearchFilters.actualPriceEnd()));
        }
        if (visitSearchFilters.actualBarberPercentage() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("actualBarberPercentage"), visitSearchFilters.actualBarberPercentage()));
        }
        if (visitSearchFilters.status() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("status"), visitSearchFilters.status()));
        }
        if (visitSearchFilters.durationMinutes() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.get("durationMinutes"), visitSearchFilters.durationMinutes()
            ));
        }
        Page<Visit> allVisits = visitService.getAll(spec, pageable);
        return allVisits
                .map(visitMapper::toResponse);
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
