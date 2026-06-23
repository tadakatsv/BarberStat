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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.barbers.*;
import ua.chekmaryov.barber_stat.service.barbers.BarberFacade;

@Validated
@RestController
@Slf4j
@RequestMapping("/api/v1/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberFacade barberFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BarberDtoResponse createBarber(
            @Valid @RequestBody BarberDtoPostRequest request) {
        return barberFacade.create(request);
    }

    @GetMapping
    public Page<BarberDtoResponse> getAllBarbers(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            BarberSearchFilters searchParameters) {
        return barberFacade.getAll(pageable, searchParameters);
    }

    @GetMapping("/{id}")
    public BarberDtoResponse getBarberById(@PathVariable Long id) {
        return barberFacade.getById(id);
    }

    @PutMapping("/{id}")
    public BarberDtoResponse updateBarberById(@PathVariable @Positive Long id, @RequestBody @Valid BarberDtoUpdateRequest request) {
        return barberFacade.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBarberById(@PathVariable @Positive Long id) {
        barberFacade.deleteById(id);
    }

}
    