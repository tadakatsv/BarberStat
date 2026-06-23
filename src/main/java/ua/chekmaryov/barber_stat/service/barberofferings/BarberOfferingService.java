package ua.chekmaryov.barber_stat.service.barberofferings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoPatchRequest;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoPostRequest;
import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoPutRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.entity.BarberOffering;
import ua.chekmaryov.barber_stat.entity.Offer;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.BarberOfferingMapper;
import ua.chekmaryov.barber_stat.repository.BarberOfferingRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BarberOfferingService {

    private final BarberOfferingRepository barberOfferingRepository;
    private final BarberOfferingMapper mapper;

    @Transactional
    public BarberOffering create(Barber barber, Offer offer, BarberOfferingDtoPostRequest request) {
        if (barberOfferingRepository.existsByBarber_IdAndOffer_Id(barber.getId(), offer.getId())) {
            throw new AlreadyExistsException("Offer already exists with this BarberId " + barber.getId() + " and OfferId " + offer.getId());
        }
        log.info("Request to make new barber offering {} for barber {} {}", offer.getName(), barber.getFirstName(), barber.getLastName());
        BarberOffering barberOffering = barberOfferingRepository.save(mapper.dtoToEntity(request, barber, offer));
        log.debug("Barber offering was made with id {}", barberOffering.getId());
        return barberOffering;
    }


    public Page<BarberOffering> getAll(Pageable pageable, Specification<BarberOffering> spec) {
        log.info("Request to get all barber offerings");
        Page<BarberOffering> allBarberOfferings = barberOfferingRepository.findAll(spec, pageable);
        log.debug("Retrieved {} records from database", allBarberOfferings.getTotalElements());
        return allBarberOfferings;
    }


    public BarberOffering getById(Long id) {
        log.info("Request to find barber offering with {} id", id);
        BarberOffering barberOffering = barberOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with ID:" + id));
        log.debug("Offer was found with name {} for barber {} {}", barberOffering.getOffer().getName(), barberOffering.getBarber().getFirstName(), barberOffering.getBarber().getLastName());
        return barberOffering;
    }


    @Transactional
    public BarberOffering updateById(Long id, BarberOfferingDtoPutRequest request) {
        log.info("Request to find barber offering with {} id", id);
        BarberOffering barberOffering = barberOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with ID:" + id));
        log.debug("Offer was found with name {} for barber {} {}", barberOffering.getOffer().getName(), barberOffering.getBarber().getFirstName(), barberOffering.getBarber().getLastName());
        return barberOfferingRepository.save(mapper.dtoUpdateToEntity(request, barberOffering));
    }

    @Transactional
    public BarberOffering patchById(Long id, BarberOfferingDtoPatchRequest request) {
        log.info("Request to find barber offering with {} id", id);
        BarberOffering barberOffering = barberOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with ID:" + id));
        log.debug("Offer was found with name {} for barber {} {}", barberOffering.getOffer().getName(), barberOffering.getBarber().getFirstName(), barberOffering.getBarber().getLastName());
        return barberOfferingRepository.save(mapper.dtoUpdateToEntity(request, barberOffering));
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (!barberOfferingRepository.existsById(id)) {
            throw new ResourceNotFoundException("No barber offering with ID:" + id);
        }
        log.info("Deleting barber offering with id {}", id);
        barberOfferingRepository.deleteById(id);
        log.debug("Deleted barberOffering with id {}", id);
        return true;
    }


    public BarberOffering findByBarberIdAndOfferId(Long barberId, Long offerId) {
        log.info("Searching for barber offering with barber id {} and offerId {}", barberId, offerId);
        BarberOffering barberOffering = barberOfferingRepository.findByBarberIdAndOfferId(barberId, offerId)
                .orElseThrow(() -> new ResourceNotFoundException("No barber offering with barberID " + barberId + "and offerID " + offerId));
        log.debug("Find barberOffering with offerId {} and barberId {}, barber offering id {}", barberId, offerId, barberOffering.getId());
        return barberOffering;
    }
}
