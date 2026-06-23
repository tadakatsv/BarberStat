package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoPatchRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoPutRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
import ua.chekmaryov.barber_stat.persistence.entity.Offer;

@Component
public class OfferMapper {
    public Offer dtoToEntity(OfferDtoPostRequest request) {
        return updateEntityFromDto(request, new Offer());
    }

    public Offer dtoToEntity(OfferDtoPatchRequest request, Offer offer) {
        return updateEntityFromDto(request, offer);
    }

    public Offer dtoToEntity(OfferDtoPutRequest request, Long id) {
        Offer offer = new Offer();
        offer.setId(id);
        return updateEntityFromDto(request, offer);
    }

    private Offer updateEntityFromDto(OfferDtoPostRequest request, Offer toUpdate) {
        toUpdate.setName(request.name().trim());
        return toUpdate;
    }

    private Offer updateEntityFromDto(OfferDtoPatchRequest request, Offer toUpdate) {
        toUpdate.setName(request.name().trim());
        return toUpdate;
    }

    private Offer updateEntityFromDto(OfferDtoPutRequest request, Offer toUpdate) {
        toUpdate.setName(request.name().trim());
        return toUpdate;
    }

    public OfferDtoResponse toResponse(Offer offer) {
        if (offer == null) return null;
        return new OfferDtoResponse(offer.getId(), offer.getName());
    }
}
