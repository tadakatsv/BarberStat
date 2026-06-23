package ua.chekmaryov.barber_stat.service.offers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.offers.*;
import ua.chekmaryov.barber_stat.persistence.entity.Offer;
import ua.chekmaryov.barber_stat.mapper.OfferMapper;

@Service
@RequiredArgsConstructor
public class OfferFacade {

    private final OfferService offerService;
    private final OfferMapper offerMapper;

    public OfferDtoResponse create(OfferDtoPostRequest request) {
        Offer offer = offerService.create(request);
        return offerMapper.toResponse(offer);
    }

    public Page<OfferDtoResponse> getAll(Pageable pageable, OfferSearchFilters offerSearchFilters) {
        Specification<Offer> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (offerSearchFilters.name() != null && !offerSearchFilters.name().isBlank()){
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("firstName")),
                    "%" + offerSearchFilters.name().trim().toLowerCase() + "%"
            ));
        }

        return offerService.getAll(pageable,spec)
                .map(offerMapper::toResponse);
    }

    public OfferDtoResponse getById(Long id){
        Offer offer = offerService.getById(id);
        return offerMapper.toResponse(offer);
    }

    public OfferDtoResponse updateById(Long id, OfferDtoPutRequest request){
        Offer offer = offerService.updateById(id,request);
        return offerMapper.toResponse(offer);
    }

    public OfferDtoResponse patchById(Long id, OfferDtoPatchRequest request){
        Offer offer = offerService.patchById(id,request);
        return offerMapper.toResponse(offer);
    }
}
