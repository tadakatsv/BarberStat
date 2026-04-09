package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;

import java.util.List;

@Component
public class BarberMapper {

    public Barber dtoToEntity(BarberDtoCreateRequest request){
        return updateEntityFromDto(request, new Barber());
    }

    public Barber dtoUpdateToEntity(Long id, BarberDtoUpdateRequest request, Barber toUpdate) {
        toUpdate.setId(id);
        return updateEntityFromDto(request, toUpdate);
    }

    private Barber updateEntityFromDto(BarberDtoCreateRequest request, Barber toUpdate) {
        toUpdate.setFirstName(request.firstName());
        toUpdate.setLastName(request.lastName());
        toUpdate.setPhone(request.phone());
        toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Barber updateEntityFromDto(BarberDtoUpdateRequest request, Barber toUpdate) {
        if (request.firstName() != null) toUpdate.setFirstName(request.firstName());
        if (request.lastName() != null) toUpdate.setLastName(request.lastName());
        if (request.phone() != null) toUpdate.setPhone(request.phone());
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.notes() != null) toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    public BarberDtoResponse toResponse(Barber barber) {
        if (barber == null) return null;
        return BarberDtoResponse.builder()
                .id(barber.getId())
                .fullName(barber.getFirstName() + " " + barber.getLastName())
                .phone(barber.getPhone())
                .status(barber.getStatus())
                .birthDate(barber.getBirthDate())
                .notes(barber.getNotes())
                .build();
    }

    public List<BarberDtoResponse> toResponseList(List<Barber> barbers){
        return barbers.stream().map(this::toResponse).toList();
    }
}
