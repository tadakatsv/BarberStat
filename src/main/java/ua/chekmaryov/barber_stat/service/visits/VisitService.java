package ua.chekmaryov.barber_stat.service.visits;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoPatchRequest;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoPutRequest;
import ua.chekmaryov.barber_stat.entity.*;
import ua.chekmaryov.barber_stat.enums.ClientStatus;
import ua.chekmaryov.barber_stat.enums.VisitStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.VisitMapper;
import ua.chekmaryov.barber_stat.repository.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper mapper;


    @Transactional
    public Visit create(VisitDtoPostRequest request, Barber barber, Offer offer, Client client, BarberOffering barberOffering) {
        Integer duration;
        if (request.durationMinutes() == null) {
            duration = barberOffering.getCustomTime();
        } else {
            duration = request.durationMinutes();
        }
        LocalDateTime visitTimeEnd = request.visitTime().plusMinutes(duration);
        if (request.visitTime().isBefore(LocalDateTime.now(ZoneId.of("Europe/Kyiv")))) {
            throw new BadRequestException("You can't make a visit on past " + request.visitTime());
        }
        if (visitRepository.hasOverlappingVisit(barber.getId(), request.visitTime(), visitTimeEnd)) {
            throw new AlreadyExistsException("Barber already booked on this time " + request.visitTime());
        }
        Visit visit = visitRepository.save(mapper.dtoToEntity(request, client, barber, offer, barberOffering));
        return visit;
    }

//    @Transactional
//    public Page<Visit> getAll(VisitStatus status, LocalDateTime visitTimeStart, LocalDateTime visitTimeEnd, Pageable pageable) {
//        if (visitTimeStart.isAfter(visitTimeEnd)) {
//            throw new BadRequestException("Start date (" + visitTimeStart + ") cannot be after end date (" + visitTimeEnd + ")");
//        }
//        log.info("Attempt to find visits with status {} between {} and {}", status, visitTimeStart, visitTimeEnd);
//        Page<Visit> neededVisits = visitRepository.findVisitsByStatusAndVisitTimeBetween(status, visitTimeStart, visitTimeEnd, pageable);
//        log.debug("Found {} visits with status {} between {} and {}", neededVisits.getNumberOfElements(), status, visitTimeStart, visitTimeEnd);
//        return neededVisits;
//    }

    public Page<Visit> getAll(Specification<Visit> spec, Pageable pageable) {
        //поставил её в Facade, но может лучше всё таки в service ?
//        if (visitTimeStart.isAfter(visitTimeEnd)) {
//            throw new BadRequestException("Start date (" + visitTimeStart + ") cannot be after end date (" + visitTimeEnd + ")");
//        }
        Page<Visit> neededVisits = visitRepository.findAll(spec, pageable);
        return neededVisits;
    }

    public Visit getById(Long id) {
        log.info("Attempt to find visit with id:{}", id);
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No visit by that id " + id));
        log.debug("Found visit with id {}", id);
        return visit;
    }

    @Transactional
    public Visit patchById(Long id, VisitDtoPatchRequest request) {
        log.info("Attempt to find visit with id:{}", id);
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No visit by that id " + id));
        log.debug("Found visit with id {}", id);
        LocalDateTime visitTimeStart;
        LocalDateTime visitTimeEnd;
        Integer duration;
        if (request.status() == VisitStatus.COMPLETED && visit.getStatus() != VisitStatus.COMPLETED) {
            Client client = visit.getClient();
            client.setLastVisitDate(visit.getVisitTime().toLocalDate());
            client.setStatus(ClientStatus.ACTIVE);
        }//должно подобное делаться тут, или в Facade ?
        if (request.durationMinutes() != null) {
            duration = request.durationMinutes();
        } else {
            duration = visit.getDurationMinutes();
        }
        if (request.visitTime() != null) {
            visitTimeStart = request.visitTime();
            visitTimeEnd = request.visitTime().plusMinutes(duration);
        } else {
            visitTimeStart = visit.getVisitTime();
            visitTimeEnd = visit.getVisitTime().plusMinutes(duration);
        }
        if (request.durationMinutes() != null || request.visitTime() != null) {
            log.debug("Check if new time is overlapping but for himself");
            if (visitRepository.hasOverlappingVisitButForHimself(visit.getBarber().getId(), visit.getId(), visitTimeStart, visitTimeEnd)) {
                throw new AlreadyExistsException("Barber already booked on this time " + visitTimeStart);
            }
        }
        log.info("Request to updated visit");
        Visit updated = visitRepository.save(mapper.dtoToEntity(request, visit));
        log.debug("Visit with id {} successfully saved", updated.getId());
        return updated;
    }

    @Transactional
    public Visit updateById(Long id, VisitDtoPutRequest request) {
        log.info("Attempt to find visit with id:{}", id);
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No visit by that id " + id));
        log.debug("Found visit with id {}", id);
        LocalDateTime visitTimeStart;
        LocalDateTime visitTimeEnd;
        Integer duration;
        if (request.status() == VisitStatus.COMPLETED && visit.getStatus() != VisitStatus.COMPLETED) {
            Client client = visit.getClient();
            client.setLastVisitDate(visit.getVisitTime().toLocalDate());
            client.setStatus(ClientStatus.ACTIVE);
        }
        duration = request.durationMinutes();
        visitTimeStart = request.visitTime();
        visitTimeEnd = request.visitTime().plusMinutes(duration);
        log.debug("Check if new time is overlapping but for himself");
        if (visitRepository.hasOverlappingVisitButForHimself(visit.getBarber().getId(), visit.getId(), visitTimeStart, visitTimeEnd)) {
            throw new AlreadyExistsException("Barber already booked on this time " + visitTimeStart);
        }
        log.info("Request to updated visit");
        Visit updated = visitRepository.save(mapper.dtoToEntity(request, visit));
        log.debug("Visit with id {} successfully saved", updated.getId());
        return updated;
    }
}
