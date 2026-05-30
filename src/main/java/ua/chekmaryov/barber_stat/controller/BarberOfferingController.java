package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoResponse;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoUpdateRequest;
import ua.chekmaryov.barber_stat.service.barberofferings.BarberOfferingService;

@RestController
@Slf4j
@RequestMapping("/api/v1/barberoffering")
public class BarberOfferingController {

    private final BarberOfferingService service;

    public BarberOfferingController(BarberOfferingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BarberOfferingDtoResponse createBarberOffering(
            @Valid @RequestBody BarberOfferingDtoCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Page<BarberOfferingDtoResponse> getAllBarberOfferings(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public BarberOfferingDtoResponse getBarberOfferingById(
            @PathVariable("id") Long id){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public BarberOfferingDtoResponse updateBarberOfferingById(
            @PathVariable("id") Long id,
            @Valid @RequestBody BarberOfferingDtoUpdateRequest request){
        return service.updateById(id,request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteBarberOfferingById(
            @PathVariable("id") Long id){
        return service.deleteById(id);
    }

    @GetMapping("/search/exact")
    public BarberOfferingDtoResponse findByBarberIdAndOfferId(
            @RequestParam("barber_id") @NotNull(message = "Barber ID is required") Long barberId,
            @RequestParam("offer_id") @NotNull(message = "Offer ID is required") Long offerId) {
        return service.findByBarberIdAndOfferId(barberId,offerId);
    }

    @GetMapping("/search/barber")
    public Page<BarberOfferingDtoResponse> findByBarber_Id(
            @RequestParam("barber_id") @NotNull(message = "Barber ID is required") Long barberId,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.findByBarber_Id(barberId,pageable);
    }

    @GetMapping("/search/offer")
    public Page<BarberOfferingDtoResponse> findByOffer_Id(
            @RequestParam("offer_id") @NotNull(message = "Offer ID is required") Long offerId,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.findByOffer_Id(offerId,pageable);
    }
}
