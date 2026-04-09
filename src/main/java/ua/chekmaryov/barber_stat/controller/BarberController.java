package ua.chekmaryov.barber_stat.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.chekmaryov.barber_stat.dto.ApiResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.service.BarberService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/barbers")
public class BarberController {
    private final BarberService service;

    public BarberController(BarberService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BarberDtoResponse>> createBarber(
            @Valid @RequestBody BarberDtoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BarberDtoResponse>>> getAllBarbers(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BarberDtoResponse>> getBarberById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BarberDtoResponse>> updateBarberById(@PathVariable("id") Long id, @Valid @RequestBody BarberDtoUpdateRequest request){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.updateById(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BarberDtoResponse>> deleteBarberById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.deleteById(id));
    }

    @GetMapping("/by-first-name-and-second-name")
    public ResponseEntity<ApiResponse<List<BarberDtoResponse>>> findBarberByFirstNameAndLastName(
            @RequestParam("firstName") final String firstName,
            @RequestParam("lastName") String lastName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findByFirstNameAndLastName(firstName, lastName));
    }

    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse<List<BarberDtoResponse>>> findBarbersByStatus(
            @RequestParam("status") final BarberStatus status) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findByStatus(status));
    }
}
    