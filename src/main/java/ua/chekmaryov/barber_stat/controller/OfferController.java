package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
import ua.chekmaryov.barber_stat.service.offers.OfferService;

@RestController
@Slf4j
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public OfferDtoResponse createOffer(
            @Valid @RequestBody OfferDtoRequest request
            ){
        return offerService.create(request);
    }

    @GetMapping
    public Page<OfferDtoResponse> getAllOffers(
            @ParameterObject @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable){
        return offerService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public OfferDtoResponse updateOfferById(
            @PathVariable("id") Long id,
            @Valid @RequestBody OfferDtoRequest request
    ){
        return offerService.updateById(id,request);
    }

    @GetMapping
    public Page<OfferDtoResponse> findByName(
            @ParameterObject @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable("name") String name
    ){
        return offerService.findByName(name, pageable);
    }

}
