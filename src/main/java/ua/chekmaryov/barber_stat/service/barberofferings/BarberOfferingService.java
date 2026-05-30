package ua.chekmaryov.barber_stat.service.barberofferings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoResponse;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoUpdateRequest;

public interface BarberOfferingService {
    BarberOfferingDtoResponse create(BarberOfferingDtoCreateRequest request);
    Page<BarberOfferingDtoResponse> getAll(Pageable pageable);
    BarberOfferingDtoResponse getById(Long id);
    BarberOfferingDtoResponse updateById(Long id, BarberOfferingDtoUpdateRequest request);
    boolean deleteById(Long id);
    BarberOfferingDtoResponse findByBarberIdAndOfferId(Long barberId, Long offerId);
    Page<BarberOfferingDtoResponse> findByBarber_Id(Long barberId, Pageable pageable);
    Page<BarberOfferingDtoResponse> findByOffer_Id(Long offerId, Pageable pageable);
}
