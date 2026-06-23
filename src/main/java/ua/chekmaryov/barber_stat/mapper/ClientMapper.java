package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoPutRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoPatchRequest;
import ua.chekmaryov.barber_stat.entity.Client;

@Component
public class ClientMapper {

    public Client dtoToEntity(ClientDtoPostRequest request) {
        return updateEntityFromDto(request, new Client());
    }

    public Client dtoToEntity(ClientDtoPatchRequest request, Client toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    public Client dtoToEntity(ClientDtoPutRequest request, Long id) {
        Client client = new Client();
        client.setId(id);
        return updateEntityFromDto(request, client);
    }

    private Client updateEntityFromDto(ClientDtoPostRequest request, Client toUpdate) {
        toUpdate.setFirstName(request.firstName().trim());
        toUpdate.setLastName(request.lastName().trim());
        toUpdate.setPhone(request.phone().replaceAll("\\s+", ""));
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.lastVisitDate() != null) toUpdate.setLastVisitDate(request.lastVisitDate());
        if (request.notes() != null) toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Client updateEntityFromDto(ClientDtoPatchRequest request, Client toUpdate) {
        if (request.firstName() != null && !request.firstName().isBlank())
            toUpdate.setFirstName(request.firstName().trim());
        if (request.lastName() != null && !request.lastName().isBlank())
            toUpdate.setLastName(request.lastName().trim());
        if (request.phone() != null && !request.phone().isBlank())
            toUpdate.setPhone(request.phone().replaceAll("\\s+", ""));
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if (request.lastVisitDate() != null) toUpdate.setLastVisitDate(request.lastVisitDate());
        if (request.notes() != null && !request.notes().isBlank()) toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Client updateEntityFromDto(ClientDtoPutRequest request, Client toUpdate) {
        toUpdate.setFirstName(request.firstName().trim());
        toUpdate.setLastName(request.lastName().trim());
        toUpdate.setPhone(request.phone().replaceAll("\\s+", ""));
        toUpdate.setBirthDate(request.birthDate());
        toUpdate.setStatus(request.status());
        toUpdate.setLastVisitDate(request.lastVisitDate());
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    public ClientDtoResponse toResponse(Client client) {
        if (client == null) return null;
        return ClientDtoResponse.builder()
                .id(client.getId())
                .fullName(client.getFirstName() + " " + client.getLastName())
                .phone(client.getPhone())
                .birthDate(client.getBirthDate())
                .lastVisitDate(client.getLastVisitDate())
                .status(client.getStatus())
                .notes(client.getNotes())
                .build();
    }

}
