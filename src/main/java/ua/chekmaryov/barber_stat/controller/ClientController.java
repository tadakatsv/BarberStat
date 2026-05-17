package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.service.ClientService;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ClientDtoResponse createClient(
            @Valid @RequestBody ClientDtoCreateRequest request){
        return clientService.create(request);
    }

    @GetMapping
    public Page<ClientDtoResponse> getAllClients(@ParameterObject @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable){
        return clientService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ClientDtoResponse getClientById(@PathVariable Long id){
        return clientService.getById(id);
    }

    @PutMapping("/{id}")
    public ClientDtoResponse updateClientById(@PathVariable("id") Long id, @Valid @RequestBody ClientDtoUpdateRequest request){
        return clientService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public ClientDtoResponse deleteClientById(@PathVariable("id") Long id){
        return clientService.deleteById(id);
    }

    @GetMapping("/by-phone")
    public ClientDtoResponse getByPhone(@RequestParam String phone){
        return clientService.getByPhone(phone);
    }

    @GetMapping("/by-first-name-and-second-name")
    public Page<ClientDtoResponse> getClientsByFirstNameAndLastName(
            @ParameterObject
            @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName
    ){
        return clientService.findByFirstNameAndLastName(firstName, lastName, pageable);
    }

    @GetMapping("/search/by-visit-date")
    public Page<ClientDtoResponse> findByLastVisitDateBetween(
            @ParameterObject
            @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam("lastVisitDateAfter") LocalDate lastVisitDateAfter,
            @RequestParam("lastVisitDateBefore") LocalDate lastVisitDateBefore
    ){
        return clientService.findByLastVisitDateBetween(lastVisitDateAfter, lastVisitDateBefore, pageable);
    }
}

