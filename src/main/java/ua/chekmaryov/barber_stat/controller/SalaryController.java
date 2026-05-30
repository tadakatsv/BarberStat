package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoResponse;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoUpdateRequest;
import ua.chekmaryov.barber_stat.service.salaries.SalaryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/salaries")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping
    public Page<SalaryDtoResponse> getAllSalaries(
            @ParameterObject @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return salaryService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public SalaryDtoResponse getById(
            @PathVariable("id") @Min(0) Long id
    ) {
        return salaryService.getById(id);
    }

    @PutMapping("/{id}")
    public SalaryDtoResponse updateSalary(
            @PathVariable("id") Long id,
            @Valid @RequestBody SalaryDtoUpdateRequest request
    ) {
        return salaryService.updateById(id, request);
    }

    @GetMapping("/barber/{barberId}")
    public Page<SalaryDtoResponse> getByBarberId(
            @PathVariable("barberId") @Min(0) Long barberId,
            @ParameterObject @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return salaryService.findByBarber_Id(barberId, pageable);
    }

    @GetMapping("/barber/{barberId}/calculate")
    public SalaryDtoResponse checkSumSalary(
            @PathVariable("barberId") @Min(0) Long barberId,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        LocalDateTime[] period = resolveDates(start, end);
        return salaryService.checkSumSalaryForBarber(barberId, period[0], period[1]);
    }

    @PostMapping("/barber/{barberId}/payout")
    @ResponseStatus(HttpStatus.CREATED)
    public SalaryDtoResponse saveSumSalary(
            @PathVariable("barberId") @Min(0) Long barberId,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        LocalDateTime[] period = resolveDates(start, end);
        return salaryService.saveSumSalaryForBarber(barberId, period[0], period[1]);
    }

    private LocalDateTime[] resolveDates(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        }
        if (end == null) {
            end = LocalDate.now().atTime(LocalTime.MAX);
        }
        return new LocalDateTime[]{start, end};
    }
}