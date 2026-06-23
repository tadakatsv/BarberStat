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
import ua.chekmaryov.barber_stat.dto.offers.*;
import ua.chekmaryov.barber_stat.service.offers.OfferFacade;

@RestController
@Slf4j
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor

public class OfferController {

    private final OfferFacade offerFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfferDtoResponse createOffer(
            @Valid @RequestBody OfferDtoPostRequest request
    ) {
        return offerFacade.create(request);
    }

    @GetMapping
    public Page<OfferDtoResponse> getAllOffers(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute OfferSearchFilters offerSearchFilters) {
        return offerFacade.getAll(pageable, offerSearchFilters);
    }

    @GetMapping("/{id}")
    public OfferDtoResponse getOfferById(
            @PathVariable("id") Long id
    ) {
        return offerFacade.getById(id);
    }

    @PutMapping("/{id}")
    public OfferDtoResponse updateOfferById(
            @PathVariable("id") Long id,
            @Valid @RequestBody OfferDtoPutRequest request
    ) {
        return offerFacade.updateById(id, request);
    }

    @PatchMapping("/{id}")
    public OfferDtoResponse patchOfferById(
            @PathVariable("id") Long id,
            @Valid @RequestBody OfferDtoPatchRequest request
    ) {
        return offerFacade.patchById(id, request);
    }

}
