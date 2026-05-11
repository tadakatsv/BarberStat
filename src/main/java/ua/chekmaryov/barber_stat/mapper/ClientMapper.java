package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Client;

@Component
public class ClientMapper {

    public Client dtoToEntity(ClientDtoCreateRequest request){
        return updateEntityFromDto(request,new Client());
    }

    public Client dtoUpdateToEntity(Long id,ClientDtoUpdateRequest request, Client toUpdate){
        toUpdate.setId(id);
        return updateEntityFromDto(request,toUpdate);
    }

    private Client updateEntityFromDto(ClientDtoCreateRequest request, Client toUpdate){
        toUpdate.setFirstName(request.firstName());
        toUpdate.setLastName(request.lastName());
        toUpdate.setPhone(request.phone());
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        toUpdate.setLastVisitDate(request.lastVisitDate());
        toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    private Client updateEntityFromDto(ClientDtoUpdateRequest request, Client toUpdate){
        if(request.firstName() != null) toUpdate.setFirstName(request.firstName());
        if(request.lastName() != null) toUpdate.setLastName(request.lastName());
        if(request.phone() != null) toUpdate.setPhone(request.phone());
        if (request.birthDate() != null) toUpdate.setBirthDate(request.birthDate());
        if (request.status() != null) toUpdate.setStatus(request.status());
        if(request.lastVisitDate() != null) toUpdate.setLastVisitDate(request.lastVisitDate());
        if(request.notes() != null) toUpdate.setNotes(request.notes());
        return toUpdate;
    }

    public ClientDtoResponse toResponse(Client client){
        if(client == null) return null;
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
