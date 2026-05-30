package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoResponse;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.*;

@Component
public class VisitMapper {
    public Visit dtoToEntity(VisitDtoCreateRequest request, Client client, Barber barber, Offer offer, BarberOffering barberOffering){
        return updateEntityFromDto(request,client,barber,offer,barberOffering, new Visit());
    }

    public Visit dtoUpdateToEntity(VisitDtoUpdateRequest request, Visit toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    private Visit updateEntityFromDto(VisitDtoCreateRequest request, Client client, Barber barber, Offer offer, BarberOffering barberOffering, Visit toUpdate) {
        toUpdate.setClient(client);
        toUpdate.setBarber(barber);
        toUpdate.setOffer(offer);
        toUpdate.setVisitTime(request.visitTime());
        if (request.actualPrice() == null){toUpdate.setActualPrice(barberOffering.getPrice());}
        else {toUpdate.setActualPrice(request.actualPrice());}
        if(request.actualBarberPercentage() == null){toUpdate.setActualBarberPercentage(barber.getSalaryPercent());}
        else {toUpdate.setActualBarberPercentage(request.actualBarberPercentage());}
        if (request.status() != null) toUpdate.setStatus(request.status());
        if(request.durationMinutes() == null){toUpdate.setDurationMinutes(barberOffering.getCustomTime());}
        else {toUpdate.setDurationMinutes(request.durationMinutes());}
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Visit updateEntityFromDto(VisitDtoUpdateRequest request, Visit toUpdate) {
        if (request.visitTime() != null) toUpdate.setVisitTime(request.visitTime());
        if (request.actualPrice() != null) toUpdate.setActualPrice(request.actualPrice());
        if (request.actualBarberPercentage() != null) toUpdate.setActualBarberPercentage(request.actualBarberPercentage());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.durationMinutes() != null) toUpdate.setDurationMinutes(request.durationMinutes());
        if (request.notes() != null && !request.notes().isBlank()) toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    public VisitDtoResponse toResponse(Visit visit) {
        if (visit == null) return null;
        return VisitDtoResponse.builder()
                .id(visit.getId())
                .clientId(visit.getClient().getId())
                .clientFullName(visit.getClient().getFirstName() + " " + visit.getClient().getLastName())
                .barberId(visit.getBarber().getId())
                .barberFullName(visit.getBarber().getFirstName() + " " + visit.getBarber().getLastName())
                .offerId(visit.getOffer().getId())
                .offerName(visit.getOffer().getName())
                .visitTime(visit.getVisitTime())
                .actualPrice(visit.getActualPrice())
                .actualBarberPercentage(visit.getActualBarberPercentage())
                .status(visit.getStatus())
                .durationMinutes(visit.getDurationMinutes())
                .notes(visit.getNotes())
                .build();
    }

}
