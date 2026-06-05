package ua.chekmaryov.barber_stat.service.barberofferings;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoResponse;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.entity.BarberOffering;
import ua.chekmaryov.barber_stat.entity.Offer;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.BarberOfferingMapper;
import ua.chekmaryov.barber_stat.repository.BarberOfferingRepository;
import ua.chekmaryov.barber_stat.repository.BarberRepository;
import ua.chekmaryov.barber_stat.repository.OfferRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarberOfferingService {

    private final BarberOfferingRepository barberOfferingRepository;
    private final BarberRepository barberRepository;
    private final OfferRepository offerRepository;
    private final BarberOfferingMapper mapper;

    @Transactional
    public BarberOffering create(BarberOfferingDtoCreateRequest request) {
        log.info("Searching for barber with id: {}" ,request.barberId());
        Barber barber = barberRepository.findById(request.barberId())
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + request.barberId()));
        log.debug("Successfully found barber: {} (ID: {})", barber.getLastName(), request.barberId());
        log.info("Request to find offer with {} id", request.offerId());
        Offer offer = offerRepository.findById(request.offerId())
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + request.offerId()));
        log.debug("Offer was found with name {}",offer.getName());
        if (barberOfferingRepository.existsByBarber_IdAndOffer_Id(barber.getId(),offer.getId())){
            throw new AlreadyExistsException("Offer already exists with this BarberId " + barber.getId() + " and OfferId " + offer.getId());
        }
        log.info("Request to make new barber offering {} for barber {} {}", offer.getName(), barber.getFirstName(),barber.getLastName());
        BarberOffering barberOffering = barberOfferingRepository.save(mapper.dtoToEntity(request,barber,offer));
        log.debug("Barber offering was made with id {}",barberOffering.getId());
        return barberOffering;
    }

    
    @Transactional
    public Page<BarberOfferingDtoResponse> getAll(Pageable pageable) {
        log.info("Request to get all barber offerings");
        Page<BarberOffering> allBarberOfferings = barberOfferingRepository.findAll(pageable);
        log.debug("Retrieved {} records from database",allBarberOfferings.getTotalElements());
        return allBarberOfferings.map(mapper::toResponse);
    }

    
    @Transactional
    public BarberOffering getById(Long id) {
        log.info("Request to find barber offering with {} id",id);
        BarberOffering barberOffering = barberOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with ID:" + id));
        log.debug("Offer was found with name {} for barber {} {}",barberOffering.getOffer().getName(), barberOffering.getBarber().getFirstName(), barberOffering.getBarber().getLastName());
        return barberOffering;
    }

    
    @Transactional
    public BarberOffering updateById(Long id, BarberOfferingDtoUpdateRequest request) {
        log.info("Request to find barber offering with {} id",id);
        BarberOffering barberOffering = barberOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with ID:" + id));
        log.debug("Offer was found with name {} for barber {} {}",barberOffering.getOffer().getName(), barberOffering.getBarber().getFirstName(), barberOffering.getBarber().getLastName());
        return barberOfferingRepository.save(mapper.dtoUpdateToEntity(request,barberOffering));
    }

    
    @Transactional
    public boolean deleteById(Long id) {
        if(!barberOfferingRepository.existsById(id)){
            throw new ResourceNotFoundException("No barber offering with ID:" + id);
        }
        log.info("Deleting barber offering with id {}", id);
        barberOfferingRepository.deleteById(id);
        log.debug("Deleted barberOffering with id {}",id);
        return true;
    }

    
    @Transactional
    public BarberOffering findByBarberIdAndOfferId(Long barberId, Long offerId) {
        log.info("Searching for barber offering with barber id {} and offerId {}",barberId,offerId);
        BarberOffering barberOffering = barberOfferingRepository.findByBarberIdAndOfferId(barberId,offerId)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with barberID " + barberId + "and offerID " + offerId));
        log.debug("Find barberOffering with offerId {} and barberId {}, barber offering id {}",barberId,offerId,barberOffering.getId());
        return barberOffering;
    }

    
    @Transactional
    public Page<BarberOfferingDtoResponse> findByBarber_Id(Long barberId, Pageable pageable) {
        log.info("Request to get all barber by barber id {} offerings",barberId);
        Page<BarberOffering> neededBarberOfferings = barberOfferingRepository.findBarberOfferingsByBarber_Id(barberId,pageable);
        log.debug("Retrieved {} records from database",neededBarberOfferings.getTotalElements());
        return neededBarberOfferings
                .map(mapper::toResponse);
    }

    
    @Transactional
    public Page<BarberOfferingDtoResponse> findByOffer_Id(Long offerId, Pageable pageable) {
        log.info("Request to get all barber by offer id {} offerings",offerId);
        Page<BarberOffering> neededBarberOfferings = barberOfferingRepository.findBarberOfferingsByOffer_Id(offerId,pageable);
        log.debug("Retrieved {} records from database",neededBarberOfferings.getTotalElements());
        return neededBarberOfferings
                .map(mapper::toResponse);
    }
}
