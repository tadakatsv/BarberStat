package ua.chekmaryov.barber_stat.service.visits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoResponse;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.VisitStatus;

import java.time.LocalDateTime;

public interface VisitService {
    VisitDtoResponse create(VisitDtoCreateRequest request);
    Page<VisitDtoResponse> getAllByStatusAndBetweenTwoDates(VisitStatus status, LocalDateTime visitTimeAfter, LocalDateTime visitTimeBefore, Pageable pageable);
    VisitDtoResponse getById(Long id);
    VisitDtoResponse updateById(Long id, VisitDtoUpdateRequest request);
    //delete использовать как то что завершилось ?
    VisitDtoResponse cancelVisitById(Long id);
    Page<VisitDtoResponse> findVisitByClient_IdAndStatus(Long clientId, VisitStatus status, Pageable pageable);
    Page<VisitDtoResponse> findByClientIdAndVisitTimeBetween(Long clientId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<VisitDtoResponse> findByVisitTimeBetween(LocalDateTime start, LocalDateTime end,Pageable pageable);
    Page<VisitDtoResponse> findByBarberIdAndVisitTimeBetween(Long barberId, LocalDateTime start, LocalDateTime end,Pageable pageable);
}
