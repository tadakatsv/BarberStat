package ua.chekmaryov.barber_stat.app.offers.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.chekmaryov.barber_stat.app.offers.OfferMapper;
import ua.chekmaryov.barber_stat.app.offers.dto.OfferDtoPatchRequest;
import ua.chekmaryov.barber_stat.app.offers.dto.OfferDtoPostRequest;
import ua.chekmaryov.barber_stat.app.offers.dto.OfferDtoPutRequest;
import ua.chekmaryov.barber_stat.app.offers.persistence.Offer;
import ua.chekmaryov.barber_stat.app.offers.persistence.OfferRepository;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Transactional
    public Offer create(OfferDtoPostRequest request) {
        log.info("Request to make new offer {}", request.name());
        if (offerRepository.existsOfferByName(request.name().trim())) {
            throw new AlreadyExistsException("Offer by name" + request.name() + "already exists");
        }
        Offer offer = offerRepository.save(offerMapper.dtoToEntity(request));
        log.debug("Offer {} was saved ID:{}", offer.getName(), offer.getId());
        return offer;
    }


    public Page<Offer> getAll(Pageable pageable, Specification<Offer> spec) {
        log.info("Request to get all offers");
        Page<Offer> allOffers = offerRepository.findAll(spec, pageable);
        log.debug("Retrieved {} records from database", allOffers.getTotalElements());
        return allOffers;
    }


    public Offer getById(Long id) {
        log.info("Request to find offer with {} id", id);
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        log.debug("Offer was found with name {}", offer.getName());
        return offer;
    }

    @Transactional
    public Offer updateById(Long id, OfferDtoPutRequest request) {
        log.info("Updating offer with ID: {}", id);
        if (!offerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offer not found with id: " + id);
        }
        if (offerRepository.existsOfferByName(request.name().trim())) {
            throw new AlreadyExistsException("Offer by name" + request.name() + "already exists");
        }
        Offer updated = offerRepository.save(offerMapper.dtoToEntity(request, id));
        log.debug("Offer ID {} successfully updated", id);
        return updated;
    }

    @Transactional
    public Offer patchById(Long id, OfferDtoPatchRequest request) {
        log.info("Updating offer with ID: {}", id);
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        if (offerRepository.existsOfferByName(request.name().trim())) {
            throw new AlreadyExistsException("Offer by name" + request.name() + "already exists");
        }
        Offer updated = offerRepository.save(offerMapper.dtoToEntity(request, offer));
        log.debug("Offer ID {} successfully updated", id);
        return updated;
    }

}
