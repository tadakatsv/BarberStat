package ua.chekmaryov.barber_stat.app.visits;

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
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoPatchRequest;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoPostRequest;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoPutRequest;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitDtoResponse;
import ua.chekmaryov.barber_stat.app.visits.dto.VisitSearchFilters;
import ua.chekmaryov.barber_stat.app.visits.service.VisitFacade;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitsController {

    private final VisitFacade visitFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDtoResponse createVisit(
            @Valid @RequestBody VisitDtoPostRequest request
    ) {
        return visitFacade.create(request);
    }

    @GetMapping
    public Page<VisitDtoResponse> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute VisitSearchFilters visitSearchFilters) {

        return visitFacade.getAll(pageable, visitSearchFilters);
    }

    @GetMapping("/{id}")
    public VisitDtoResponse getById(
            @PathVariable("id") @Positive Long id
    ) {
        return visitFacade.getById(id);
    }

    @PutMapping("/{id}")
    public VisitDtoResponse updateById(
            @PathVariable("id") @Positive Long id,
            @RequestBody @Valid VisitDtoPutRequest request) {
        return visitFacade.updateById(id, request);
    }

    @PatchMapping("/{id}")
    public VisitDtoResponse patchById(
            @PathVariable("id") @Positive Long id,
            @RequestBody @Valid VisitDtoPatchRequest request
    ) {
        return visitFacade.patchById(id, request);
    }


}
