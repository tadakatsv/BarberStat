package ua.chekmaryov.barber_stat.service.offers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;

public interface OfferService {
    OfferDtoResponse create(OfferDtoRequest request);
    Page<OfferDtoResponse> getAll(Pageable pageable);
    OfferDtoResponse getById(Long id);
    OfferDtoResponse updateById(Long id, OfferDtoRequest request);
    Page<OfferDtoResponse> findByName(String name,Pageable pageable);
}
