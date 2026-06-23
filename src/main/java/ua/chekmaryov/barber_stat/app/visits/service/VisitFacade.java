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
import ua.chekmaryov.barber_stat.app.visits.persistence.VisitSpecifications;

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

    public Page<VisitDtoResponse> getAll(Pageable pageable, VisitSearchFilters filters) {
        return visitService.getAll(filters, pageable)
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
