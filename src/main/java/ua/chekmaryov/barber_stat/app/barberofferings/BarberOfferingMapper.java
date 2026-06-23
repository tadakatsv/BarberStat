package ua.chekmaryov.barber_stat.app.barberofferings;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.app.barberofferings.dto.BarberOfferingDtoPatchRequest;
import ua.chekmaryov.barber_stat.app.barberofferings.dto.BarberOfferingDtoPostRequest;
import ua.chekmaryov.barber_stat.app.barberofferings.dto.BarberOfferingDtoResponse;
import ua.chekmaryov.barber_stat.app.barberofferings.dto.BarberOfferingDtoPutRequest;
import ua.chekmaryov.barber_stat.app.barbers.persistence.Barber;
import ua.chekmaryov.barber_stat.app.barberofferings.persistence.BarberOffering;
import ua.chekmaryov.barber_stat.app.offers.persistence.Offer;

@Component
public class BarberOfferingMapper {
    public BarberOffering dtoToEntity(BarberOfferingDtoPostRequest request, Barber barber, Offer offer) {
        return updateEntityFromDto(request, barber, offer, new BarberOffering());
    }

    public BarberOffering dtoUpdateToEntity(BarberOfferingDtoPatchRequest request, BarberOffering toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    public BarberOffering dtoUpdateToEntity(BarberOfferingDtoPutRequest request, BarberOffering toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    private BarberOffering updateEntityFromDto(BarberOfferingDtoPostRequest request, Barber barber, Offer offer, BarberOffering toUpdate) {
        toUpdate.setBarber(barber);
        toUpdate.setOffer(offer);
        toUpdate.setPrice(request.price());
        toUpdate.setCustomTime(request.customTime());
        return toUpdate;
    }

    private BarberOffering updateEntityFromDto(BarberOfferingDtoPutRequest request, BarberOffering toUpdate) {
        toUpdate.setPrice(request.price());
        toUpdate.setCustomTime(request.customTime());
        return toUpdate;
    }

    private BarberOffering updateEntityFromDto(BarberOfferingDtoPatchRequest request, BarberOffering toUpdate) {
        if (request.price() != null) toUpdate.setPrice(request.price());
        if (request.customTime() != null) toUpdate.setCustomTime(request.customTime());
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
