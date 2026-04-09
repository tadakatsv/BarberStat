package ua.chekmaryov.barber_stat.service;

import ua.chekmaryov.barber_stat.dto.ApiResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.BarberStatus;

import java.util.List;

public interface BarberService {
    ApiResponse<BarberDtoResponse> create(BarberDtoCreateRequest request);
    ApiResponse<List<BarberDtoResponse>> getAll();
    ApiResponse<BarberDtoResponse> getById(Long id);
    ApiResponse<BarberDtoResponse> updateById(Long id, BarberDtoUpdateRequest request);
    ApiResponse<BarberDtoResponse> deleteById(Long id);
    ApiResponse<List<BarberDtoResponse>> findByFirstNameAndLastName(String firstName, String lastName);
    ApiResponse<List<BarberDtoResponse>> findByStatus(BarberStatus status);
}
