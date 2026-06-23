package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.clients.*;
import ua.chekmaryov.barber_stat.service.clients.ClientFacade;

@RestController
@Slf4j
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientFacade clientFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDtoResponse createClient(
            @Valid @RequestBody ClientDtoPostRequest request) {
        return clientFacade.create(request);
    }

    @GetMapping
    public Page<ClientDtoResponse> getAllClients(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute ClientSearchFilters clientSearchFilters) {
        return clientFacade.getAll(pageable, clientSearchFilters);
    }

    @GetMapping("/{id}")
    public ClientDtoResponse getClientById(
            @PathVariable Long id) {
        return clientFacade.getById(id);
    }

    @PatchMapping("/{id}")
    public ClientDtoResponse patchClientById(
            @PathVariable("id") Long id,
            @Valid @RequestBody ClientDtoPatchRequest request) {
        return clientFacade.patchById(id, request);
    }

    @PutMapping
    public ClientDtoResponse putById(
            @PathVariable("id") Long id,
            @Valid @RequestBody ClientDtoPutRequest request
    ) {
        return clientFacade.updateById(id, request);
    }
}

