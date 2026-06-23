package ua.chekmaryov.barber_stat.service.barberofferings;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.barberOffering.*;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.entity.BarberOffering;
import ua.chekmaryov.barber_stat.entity.Offer;
import ua.chekmaryov.barber_stat.mapper.BarberOfferingMapper;
import ua.chekmaryov.barber_stat.service.barbers.BarberService;
import ua.chekmaryov.barber_stat.service.offers.OfferService;

@Service
@RequiredArgsConstructor
public class BarberOfferingFacade {
    private final BarberOfferingService barberOfferingService;
    private final BarberOfferingMapper barberOfferingMapper;
    private final BarberService barberService;
    private final OfferService offerService;

    public BarberOfferingDtoResponse create(BarberOfferingDtoPostRequest request) {
        Barber barber = barberService.getById(request.barberId());
        Offer offer = offerService.getById(request.offerId());
        BarberOffering barberOffering = barberOfferingService.create(barber, offer, request);
        return barberOfferingMapper.toResponse(barberOffering);
    }

    public Page<BarberOfferingDtoResponse> getAll(Pageable pageable, BarberOfferingSearchFilters barberOfferingSearchFilters) {
        Specification<BarberOffering> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (barberOfferingSearchFilters.barberId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.join("barber").get("id"), barberOfferingSearchFilters.barberId()
            ));
        }
        if (barberOfferingSearchFilters.offerId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    root.join("offer").get("id"), barberOfferingSearchFilters.offerId()
            ));
        }
        if (barberOfferingSearchFilters.price() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                            root.get("price"), barberOfferingSearchFilters.price()
                    )
            );
        }
        if (barberOfferingSearchFilters.customTime() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                            root.get("customTime"), barberOfferingSearchFilters.customTime()
                    )
            );
        }
        return barberOfferingService.getAll(pageable, spec)
                .map(barberOfferingMapper::toResponse);
    }

    public BarberOfferingDtoResponse getById(Long id) {
        BarberOffering barberOffering = barberOfferingService.getById(id);
        return barberOfferingMapper.toResponse(barberOffering);
    }

    public BarberOfferingDtoResponse updateById(Long id, BarberOfferingDtoPutRequest request) {
        BarberOffering barberOffering = barberOfferingService.updateById(id, request);
        return barberOfferingMapper.toResponse(barberOffering);
    }

    public BarberOfferingDtoResponse patchById(Long id, BarberOfferingDtoPatchRequest request) {
        BarberOffering barberOffering = barberOfferingService.patchById(id, request);
        return barberOfferingMapper.toResponse(barberOffering);
    }

    public boolean deleteById(Long id) {
        return barberOfferingService.deleteById(id);
    }

}
