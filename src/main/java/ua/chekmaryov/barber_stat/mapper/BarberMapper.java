package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPutRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoPatchRequest;
import ua.chekmaryov.barber_stat.entity.Barber;

@Component
public class BarberMapper {

    public Barber dtoToEntity(BarberDtoPostRequest request) {
        return updateEntityFromDto(request, new Barber());
    }

    public Barber dtoToEntity(BarberDtoPatchRequest request, Barber toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    public Barber dtoToEntity(Long id, BarberDtoPutRequest request) {
        Barber barber = new Barber();
        barber.setId(id);
        return updateEntityFromDto(request, barber);
    }

    private Barber updateEntityFromDto(BarberDtoPostRequest request, Barber toUpdate) {
        toUpdate.setFirstName(request.firstName().trim());
        toUpdate.setLastName(request.lastName().trim());
        toUpdate.setPhone(request.phone().replaceAll("\\s+", ""));
        toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.role() != null) toUpdate.setRole(request.role());
        if (request.salaryPercent() != null) toUpdate.setSalaryPercent(request.salaryPercent());
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Barber updateEntityFromDto(BarberDtoPutRequest request, Barber toUpdate) {
        toUpdate.setFirstName(request.firstName().trim());
        toUpdate.setLastName(request.lastName().trim());
        toUpdate.setPhone(request.phone().replaceAll("\\s+", ""));
        toUpdate.setBirthDate(request.birthDate());
        toUpdate.setStatus(request.status());
        toUpdate.setRole(request.role());
        toUpdate.setSalaryPercent(request.salaryPercent());
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Barber updateEntityFromDto(BarberDtoPatchRequest request, Barber toUpdate) {
        if (request.firstName() != null && !request.firstName().isBlank())
            toUpdate.setFirstName(request.firstName().trim());
        if (request.lastName() != null && !request.lastName().isBlank())
            toUpdate.setLastName(request.lastName().trim());
        if (request.phone() != null && !request.phone().isBlank())
            toUpdate.setPhone(request.phone().replaceAll("\\s+", ""));
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.role() != null) toUpdate.setRole(request.role());
        if (request.salaryPercent() != null) toUpdate.setSalaryPercent(request.salaryPercent());
        if (request.notes() != null && !request.notes().isBlank()) toUpdate.setNotes(request.notes());
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
