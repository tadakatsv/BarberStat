package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.service.BarberService;

@RestController
@Slf4j
@RequestMapping("/api/v1/barbers")
public class BarberController {
    private final BarberService service;

    public BarberController(BarberService service) {
        this.service = service;
    }

    @PostMapping
    public BarberDtoResponse createBarber(
            @Valid @RequestBody BarberDtoCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Page<BarberDtoResponse> getAllBarbers(@ParameterObject @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC)Pageable pageable){
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public BarberDtoResponse getBarberById(@PathVariable("id") Long id){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public BarberDtoResponse updateBarberById(@PathVariable("id") Long id, @Valid @RequestBody BarberDtoUpdateRequest request){
        return service.updateById(id,request);
    }

    @DeleteMapping("/{id}")
    public BarberDtoResponse deleteBarberById(@PathVariable("id") Long id){
        return service.deleteById(id);
    }

    @GetMapping("/by-first-name-and-second-name")
    public Page<BarberDtoResponse> findBarberByFirstNameAndLastName(
            @RequestParam("firstName") final String firstName,
            @RequestParam("lastName") String lastName,
            @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.findByFirstNameAndLastName(firstName, lastName,pageable);
    }

    @GetMapping("/by-status")
    public Page<BarberDtoResponse> findBarbersByStatus(
            @RequestParam("status") final BarberStatus status,
            @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.findByStatus(status,pageable);
    }
}
    