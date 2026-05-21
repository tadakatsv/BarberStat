package ua.chekmaryov.barber_stat.service.offers;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
import ua.chekmaryov.barber_stat.entity.Offer;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.OfferMapper;
import ua.chekmaryov.barber_stat.repository.OfferRepository;

@Slf4j
@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;

    public OfferServiceImpl(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }


    @Override
    @Transactional
    public OfferDtoResponse create(OfferDtoRequest request) {
        log.info("Request to make new offer {}", request.name());
        Offer offer = offerRepository.save(offerMapper.dtoToEntity(request));
        log.debug("Offer {} was saved ID:{}",offer.getName(),offer.getId());
        return offerMapper.toResponse(offer);
    }

    @Override
    @Transactional
    public Page<OfferDtoResponse> getAll(Pageable pageable) {
        log.info("Request to get all offers");
        Page<Offer> allOffers = offerRepository.findAll(pageable);
        log.debug("Retrieved {} records from database",allOffers.getTotalElements());
        return allOffers.map(offerMapper::toResponse);
    }

    @Override
    @Transactional
    public OfferDtoResponse getById(Long id) {
        log.info("Request to find offer with {} id",id);
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        log.debug("Offer was found with name {}",offer.getName());
        return offerMapper.toResponse(offer);
    }

    @Override
    @Transactional
    public OfferDtoResponse updateById(Long id, OfferDtoRequest request) {
        log.info("Updating offer with ID: {}",id);
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        Offer updated = offerRepository.save(offerMapper.dtoUpdateToEntity(request,offer));
        log.debug("Offer ID {} successfully updated", id);
        return offerMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public Page<OfferDtoResponse> findByName(String name, Pageable pageable) {
        log.info("Request to find offer {}",name);
        Page<Offer> offers = offerRepository.findByNameContainingIgnoreCase(name, pageable);
        log.debug("Was found {} element(s)",offers.getTotalElements());
        return offers.map(offerMapper::toResponse);
    }
}
