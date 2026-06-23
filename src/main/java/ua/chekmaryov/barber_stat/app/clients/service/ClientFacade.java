package ua.chekmaryov.barber_stat.app.clients.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.app.clients.ClientMapper;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoPatchRequest;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoPostRequest;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoPutRequest;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientDtoResponse;
import ua.chekmaryov.barber_stat.app.clients.dto.ClientSearchFilters;
import ua.chekmaryov.barber_stat.app.clients.persistence.Client;

//нужен ли @Transactional
@Service
@RequiredArgsConstructor
public class ClientFacade {
    private final ClientMapper clientMapper;
    private final ClientService clientService;

    public ClientDtoResponse create(ClientDtoPostRequest request) {
        Client client = clientService.create(request);
        return clientMapper.toResponse(client);
    }


    public Page<ClientDtoResponse> getAll(Pageable pageable, ClientSearchFilters clientSearchFilters) {
        Specification<Client> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (clientSearchFilters.firstName() != null && !clientSearchFilters.firstName().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("firstName")),
                    "%" + clientSearchFilters.firstName().trim().toLowerCase() + "%"
            ));
        }

        if (clientSearchFilters.lastName() != null && !clientSearchFilters.lastName().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("lastName")),
                    "%" + clientSearchFilters.lastName().trim().toLowerCase() + "%"
            ));
        }

        if (clientSearchFilters.phone() != null && !clientSearchFilters.phone().isBlank()) {
            String cleanPhone = clientSearchFilters.phone().replaceAll("\\s+", "");
            spec = spec.and((root, query, cb) -> cb.like(
                    root.get("phone"), "%" + cleanPhone + "%"));
        }

        if (clientSearchFilters.status() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), clientSearchFilters.status()));
        }

        if (clientSearchFilters.birthDate() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("birthDate"), clientSearchFilters.birthDate()));
        }

        if (clientSearchFilters.lastVisitDate() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("lastVisitDate"), clientSearchFilters.lastVisitDate()));
        }

        return clientService.getAll(pageable, spec)
                .map(clientMapper::toResponse);
    }


    public ClientDtoResponse getById(Long id) {
        Client byId = clientService.getById(id);
        return clientMapper.toResponse(byId);
    }


    public ClientDtoResponse patchById(Long id, ClientDtoPatchRequest request) {
        Client client = clientService.patchById(id, request);
        return clientMapper.toResponse(client);
    }

    public ClientDtoResponse updateById(Long id, ClientDtoPutRequest request) {
        Client client = clientService.updateById(id, request);
        return clientMapper.toResponse(client);
    }
}
