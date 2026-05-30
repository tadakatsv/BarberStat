package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoResponse;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.VisitStatus;
import ua.chekmaryov.barber_stat.service.visits.VisitService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@Slf4j
@RequestMapping("/api/v1/visits")
public class VisitsController {

    private final VisitService service;

    public VisitsController(VisitService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDtoResponse createVisit(
            @Valid @RequestBody VisitDtoCreateRequest request
            ){
        return service.create(request);
    }

    @GetMapping//я хочу чтоб не было getAll, ибо не имеет смысла. Хотя тут Page может просто, чтоб сортировало с конца. Не знаю как именно нагружается база
    public Page<VisitDtoResponse> getAllByStatusAndBetweenTwoDates(
            @RequestParam(value = "status", required = false, defaultValue = "PLANNED") VisitStatus status,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (start == null) {
            start = LocalDate.now().atStartOfDay();
        }
        if (end == null) {
            end = LocalDate.now().atTime(LocalTime.MAX);
        }

        return service.getAllByStatusAndBetweenTwoDates(status, start, end, pageable);
    }

    @GetMapping("/{id}")
    public VisitDtoResponse getById(
            @PathVariable("id") @Positive Long id
    ){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public VisitDtoResponse updateBarberById(
            @PathVariable("id") @Positive Long id,
            @RequestBody @Valid VisitDtoUpdateRequest request){
        return service.updateById(id,request);
    }

    @DeleteMapping("/{id}")
    public VisitDtoResponse cancelById(
            @PathVariable("id") @Positive Long id
    ){
        return service.cancelVisitById(id);
    }

    @GetMapping("/search/by-client_id-and-status")
    public Page<VisitDtoResponse> findVisitByClient_IdAndStatus(
            @RequestParam(name = "client_id") @Positive Long clientId,
            @RequestParam(name = "status") VisitStatus status,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return service.findVisitByClient_IdAndStatus(clientId, status, pageable);
    }

    @GetMapping("/search/by-client_id-and-visit-time-between")
    public Page<VisitDtoResponse> findByClientIdAndVisitTimeBetween(
            @RequestParam(name = "client_id") @Positive  Long clientId,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return service.findByClientIdAndVisitTimeBetween(clientId, start, end, pageable);
    }

    @GetMapping("/search/by-visit-time-between")
    public Page<VisitDtoResponse> findByVisitTimeBetween(
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return service.findByVisitTimeBetween(start, end, pageable);
    }

    @GetMapping("/search/by-barber-id-and-visit-time-between")
    public Page<VisitDtoResponse> findByBarberIdAndVisitTimeBetween(
            @RequestParam(name = "barber_id") @Min(0) Long barberId,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return service.findByBarberIdAndVisitTimeBetween(barberId, start, end, pageable);
    }


}
