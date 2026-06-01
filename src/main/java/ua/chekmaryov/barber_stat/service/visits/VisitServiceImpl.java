package ua.chekmaryov.barber_stat.service.visits;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoResponse;
import ua.chekmaryov.barber_stat.dto.visits.VisitDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.*;
import ua.chekmaryov.barber_stat.enums.VisitStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.VisitMapper;
import ua.chekmaryov.barber_stat.repository.*;

import java.time.LocalDateTime;

@Service
@Slf4j
public class VisitServiceImpl implements VisitService{

    private final BarberRepository barberRepository;

    private final ClientRepository clientRepository;

    private final OfferRepository offerRepository;

    private final VisitRepository visitRepository;

    private final BarberOfferingRepository barberOfferingRepository;

    private final VisitMapper mapper;

    public VisitServiceImpl(BarberRepository barberRepository, ClientRepository clientRepository, OfferRepository offerRepository, VisitRepository visitRepository, VisitMapper mapper, BarberOfferingRepository barberOfferingRepository) {
        this.barberRepository = barberRepository;
        this.clientRepository = clientRepository;
        this.offerRepository = offerRepository;
        this.visitRepository = visitRepository;
        this.mapper = mapper;
        this.barberOfferingRepository = barberOfferingRepository;
    }


    @Override
    @Transactional
    public VisitDtoResponse create(VisitDtoCreateRequest request) {
        log.info("Searching for barber with id: {}" ,request.barberId());
        Barber barber = barberRepository.findById(request.barberId())
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + request.barberId()));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), request.barberId());
        log.info("Request to find offer with {} id", request.offerId());
        Offer offer = offerRepository.findById(request.offerId())
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + request.offerId()));
        log.debug("Offer was found with name {}",offer.getName());
        log.info("Searching for client with id: {}" , request.clientId());
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.clientId()));
        log.debug("Successfully found client: {} (ID: {})", client.getLastName(), request.clientId());
        BarberOffering barberOffering = barberOfferingRepository.findByBarberIdAndOfferId(request.barberId(), request.offerId())
                .orElseThrow(() -> new ResourceNotFoundException("Barber " + barber.getFirstName() + " " + barber.getLastName() + " don't have this offer " + offer.getName() ));
        Integer duration;
        if (request.durationMinutes() == null){
            duration = barberOffering.getCustomTime();
        }
        else {duration = request.durationMinutes();}
        LocalDateTime visitTimeEnd = request.visitTime().plusMinutes(duration);
        if (request.visitTime().isBefore(LocalDateTime.now())){
            throw new BadRequestException("You can't make a visit on past");
        }
        if (visitRepository.hasOverlappingVisit(barber.getId(),request.visitTime(),visitTimeEnd)){
            throw new AlreadyExistsException("Barber already booked on this time " + request.visitTime());
        }
        Visit visit = visitRepository.save(mapper.dtoToEntity(request,client,barber,offer,barberOffering));
        return mapper.toResponse(visit);
    }

    @Override
    @Transactional
    public Page<VisitDtoResponse> getAllByStatusAndBetweenTwoDates(VisitStatus status, LocalDateTime visitTimeAfter, LocalDateTime visitTimeBefore, Pageable pageable) {
        log.info("Attempt to find visits with status {} between {} and {}",status,visitTimeAfter,visitTimeBefore);
        Page<Visit> neededVisits = visitRepository.findVisitsByStatusAndVisitTimeBetween(status,visitTimeAfter,visitTimeBefore,pageable);
        log.debug("Found {} visits with status {} between {} and {}", neededVisits.getNumberOfElements(),status,visitTimeAfter,visitTimeBefore);
        return neededVisits
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public VisitDtoResponse getById(Long id) {
        log.info("Attempt to find visit with id:{}",id);
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No visit by that id"));
        log.debug("Found visit with id {}",id);
        return mapper.toResponse(visit);
    }

    @Override
    @Transactional
    public VisitDtoResponse updateById(Long id, VisitDtoUpdateRequest request) {
        log.info("Attempt to find visit with id:{}",id);
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No visit by that id"));
        log.debug("Found visit with id {}",id);
        LocalDateTime visitTimeStart;
        LocalDateTime visitTimeEnd;
        Integer duration;
        if(request.status() == VisitStatus.COMPLETED && visit.getStatus() != VisitStatus.COMPLETED){
            Client client = visit.getClient();
            client.setLastVisitDate(visit.getVisitTime().toLocalDate());
        }
        if (request.durationMinutes() !=null) {
            duration = request.durationMinutes();
        }
        else {
            duration = visit.getDurationMinutes();
        }
        if (request.visitTime() != null) {
                visitTimeStart = request.visitTime();
                visitTimeEnd = request.visitTime().plusMinutes(duration);
        } else {
                visitTimeStart = visit.getVisitTime();
                visitTimeEnd = visit.getVisitTime().plusMinutes(duration);
        }
        if (request.durationMinutes() !=null || request.visitTime() != null) {
            log.debug("Check if new time is overlapping but for himself");
            if (visitRepository.hasOverlappingVisitButForHimself(visit.getBarber().getId(), visit.getId(), visitTimeStart, visitTimeEnd)) {
                    throw new AlreadyExistsException("Barber already booked on this time " + visitTimeStart);
            }
        }
        log.info("Request to updated visit");
        Visit updated = visitRepository.save(mapper.dtoUpdateToEntity(request,visit));
        log.debug("Visit with id {} successfully saved", updated.getId());
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public VisitDtoResponse cancelVisitById(Long id) {
        log.debug("Attempting to cancel visit with ID: {}", id);
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No visit by that id"));
        visit.setStatus(VisitStatus.CANCELLED);
        log.info("Visit with ID: {} was successfully CANCELLED", id);
        return mapper.toResponse(visit);
    }

    @Override
    @Transactional
    public Page<VisitDtoResponse> findVisitByClient_IdAndStatus(Long clientId, VisitStatus status, Pageable pageable) {
        log.debug("Searching visits for client ID: {} with status {}", clientId, status);
        Page<Visit> neededVisits = visitRepository.findVisitsByClient_IdAndStatus(clientId,status,pageable);
        log.debug("Found {} visits for client ID: {}", neededVisits.getNumberOfElements(), clientId);
        return neededVisits
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public Page<VisitDtoResponse> findByClientIdAndVisitTimeBetween(Long clientId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        log.debug("Searching visits for client ID: {} between {} and {}", clientId, start, end);
        if (start.isAfter(end)){
            throw new BadRequestException("Start date (" + start + ") cannot be after end date (" + end + ")");
        }
        Page<Visit> neededVisits = visitRepository.findByClientIdAndVisitTimeBetween(clientId, start, end, pageable);
        log.debug("Found {} visits for client ID: {}", neededVisits.getNumberOfElements(), clientId);
        return neededVisits
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public Page<VisitDtoResponse> findByVisitTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        log.info("Request to find visits between {} and {}",start,end);
        if (start.isAfter(end)){
            throw new BadRequestException("Start date (" + start + ") cannot be after end date (" + end + ")");
        }
        Page<Visit> neededVisits = visitRepository.findByVisitTimeBetween(start, end, pageable);
        log.debug("Found {} visits", neededVisits.getNumberOfElements());
        return neededVisits
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public Page<VisitDtoResponse> findByBarberIdAndVisitTimeBetween(Long barberId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        log.info("Attempt to find visits with barberID {} between {} and {}",barberId,start,end);
        if (start.isAfter(end)){
            throw new BadRequestException("Start date (" + start + ") cannot be after end date (" + end + ")");
        }
        Page<Visit> neededVisits = visitRepository.findByBarberIdAndVisitTimeBetween(barberId, start, end, pageable);
        log.debug("Found {} needed visits for barber ID: {}",neededVisits.getTotalElements(),barberId);
        return neededVisits
                .map(mapper::toResponse);
    }
}
