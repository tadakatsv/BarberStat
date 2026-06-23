package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.barberOffering.*;
import ua.chekmaryov.barber_stat.service.barberofferings.BarberOfferingFacade;

@RestController
@Slf4j
@RequestMapping("/api/v1/barberoffering")
@RequiredArgsConstructor
public class BarberOfferingController {

    private final BarberOfferingFacade service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BarberOfferingDtoResponse createBarberOffering(
            @Valid @RequestBody BarberOfferingDtoPostRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Page<BarberOfferingDtoResponse> getAllBarberOfferings(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute BarberOfferingSearchFilters barberOfferingSearchFilters
    ) {
        return service.getAll(pageable, barberOfferingSearchFilters);
    }

    @GetMapping("/{id}")
    public BarberOfferingDtoResponse getBarberOfferingById(
            @PathVariable("id") @Positive(message = "Id must be positive always") Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public BarberOfferingDtoResponse updateBarberOfferingById(
            @PathVariable("id") @Positive(message = "Id must be positive always") Long id,
            @Valid @RequestBody BarberOfferingDtoPutRequest request) {
        return service.updateById(id, request);
    }

    @PatchMapping("/{id}")
    public BarberOfferingDtoResponse patchBaberOfferingById(
            @PathVariable("id") @Positive(message = "Id must be positive always") Long id,
            @Valid @RequestBody BarberOfferingDtoPatchRequest request
    ) {
        return service.patchById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteBarberOfferingById(
            @PathVariable("id") @Positive(message = "Id must be positive always") Long id) {
        return service.deleteById(id);
    }

    //Не сделал тут findByBarberIdAndOfferId и в Facade
}
