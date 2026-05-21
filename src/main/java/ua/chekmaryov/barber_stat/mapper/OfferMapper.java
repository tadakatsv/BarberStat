package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
import ua.chekmaryov.barber_stat.entity.Offer;

@Component
public class OfferMapper {
    public Offer dtoToEntity(OfferDtoRequest request){
        return updateEntityFromDto(request,new Offer());
    }

    public Offer dtoUpdateToEntity(OfferDtoRequest request,Offer offer){
        return updateEntityFromDto(request, offer);
    }

    private Offer updateEntityFromDto(OfferDtoRequest request, Offer toUpdate){
        toUpdate.setName(request.name().trim());
        return toUpdate;
    }

    public OfferDtoResponse toResponse(Offer offer){
        if(offer == null) return null;
        return new OfferDtoResponse(offer.getId(),offer.getName());
    }
}
