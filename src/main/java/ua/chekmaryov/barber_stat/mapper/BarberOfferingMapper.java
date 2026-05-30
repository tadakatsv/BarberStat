package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoResponse;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.entity.BarberOffering;
import ua.chekmaryov.barber_stat.entity.Offer;

@Component
public class BarberOfferingMapper {
    public BarberOffering dtoToEntity(BarberOfferingDtoCreateRequest request,Barber barber,Offer offer){
        return updateEntityFromDto(request,barber,offer, new BarberOffering());
    }

    public BarberOffering dtoUpdateToEntity(BarberOfferingDtoUpdateRequest request, BarberOffering toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    private BarberOffering updateEntityFromDto(BarberOfferingDtoCreateRequest request,Barber barber,Offer offer, BarberOffering toUpdate) {
        toUpdate.setBarber(barber);
        toUpdate.setOffer(offer);
        toUpdate.setPrice(request.price());
        toUpdate.setCustomTime(request.customTime());
        return toUpdate;
    }

    private BarberOffering updateEntityFromDto(BarberOfferingDtoUpdateRequest request, BarberOffering toUpdate) {
        if(request.price() != null) toUpdate.setPrice(request.price());
        if(request.customTime() != null) toUpdate.setCustomTime(request.customTime());
        return toUpdate;
    }

    public BarberOfferingDtoResponse toResponse(BarberOffering barberOffering) {
        if (barberOffering == null) return null;
        return BarberOfferingDtoResponse.builder()
                .id(barberOffering.getId())
                .barberId(barberOffering.getBarber().getId())
                .barberFullName(barberOffering.getBarber().getFirstName() + " " + barberOffering.getBarber().getLastName())
                .offerId(barberOffering.getOffer().getId())
                .offerName(barberOffering.getOffer().getName())
                .price(barberOffering.getPrice())
                .customTime(barberOffering.getCustomTime())
                .build();
    }
}
