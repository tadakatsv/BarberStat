package ua.chekmaryov.barber_stat.service.offers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
import ua.chekmaryov.barber_stat.entity.Offer;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.OfferMapper;
import ua.chekmaryov.barber_stat.repository.OfferRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Transactional
    public Offer create(OfferDtoRequest request) {
        log.info("Request to make new offer {}", request.name());
        if (offerRepository.existsOfferByName(request.name().trim())) {
            throw new AlreadyExistsException("Offer by name" + request.name() + "already exists");
        }
        Offer offer = offerRepository.save(offerMapper.dtoToEntity(request));
        log.debug("Offer {} was saved ID:{}", offer.getName(), offer.getId());
        return offer;
    }

    
    @Transactional
    public Page<OfferDtoResponse> getAll(Pageable pageable) {
        log.info("Request to get all offers");
        Page<Offer> allOffers = offerRepository.findAll(pageable);
        log.debug("Retrieved {} records from database", allOffers.getTotalElements());
        return allOffers.map(offerMapper::toResponse);
    }

    
    @Transactional
    public Offer getById(Long id) {
        log.info("Request to find offer with {} id", id);
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        log.debug("Offer was found with name {}", offer.getName());
        return offer;
    }

    
    @Transactional
    public Offer updateById(Long id, OfferDtoRequest request) {
        log.info("Updating offer with ID: {}", id);
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        if (offerRepository.existsOfferByName(request.name().trim())) {
            throw new AlreadyExistsException("Offer by name" + request.name() + "already exists");
        }
        Offer updated = offerRepository.save(offerMapper.dtoUpdateToEntity(request, offer));
        log.debug("Offer ID {} successfully updated", id);
        return offer;
    }

    
    @Transactional
    public Page<OfferDtoResponse> findByName(String name, Pageable pageable) {
        log.info("Request to find offer {}", name);
        Page<Offer> offers = offerRepository.findByNameContainingIgnoreCase(name, pageable);
        log.debug("Was found {} element(s)", offers.getTotalElements());
        return offers.map(offerMapper::toResponse);
    }
}
