package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;

@Component
public class BarberMapper {

    public Barber dtoToEntity(BarberDtoCreateRequest request){
        return updateEntityFromDto(request, new Barber());
    }

    public Barber dtoUpdateToEntity(BarberDtoUpdateRequest request, Barber toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    private Barber updateEntityFromDto(BarberDtoCreateRequest request, Barber toUpdate) {
        toUpdate.setFirstName(request.firstName());
        toUpdate.setLastName(request.lastName());
        toUpdate.setPhone(request.phone());
        toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        toUpdate.setRole(request.role());
        toUpdate.setSalaryPercent(request.salaryPercent());
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Barber updateEntityFromDto(BarberDtoUpdateRequest request, Barber toUpdate) {
        if (request.firstName() != null) toUpdate.setFirstName(request.firstName());
        if (request.lastName() != null) toUpdate.setLastName(request.lastName());
        if (request.phone() != null) toUpdate.setPhone(request.phone());
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.role() != null) toUpdate.setRole(request.role());
        if (request.salaryPercent() != null) toUpdate.setSalaryPercent(request.salaryPercent());
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
                .role(barber.getRole())
                .salaryPercent(barber.getSalaryPercent())
                .notes(barber.getNotes())
                .build();
    }

}
