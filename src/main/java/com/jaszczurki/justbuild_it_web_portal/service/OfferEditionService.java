package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.dto.OfferDto;
import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.mapper.OfferMapper;
import com.jaszczurki.justbuild_it_web_portal.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class OfferEditionService {

    private final OfferRepository offerRepository;
    private final OfferMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferEditionService.class);

    public OfferDto findOfferDtoById(UUID id) {
        LOGGER.debug("Fetching offer with ID: {}", id);
        return offerRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow();
    }

    public Offer updateOffer(OfferDto editedOfferDto) {
        Offer existingOffer = offerRepository.findById(editedOfferDto.getDtoOfferId())
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        existingOffer.setCity(editedOfferDto.getCity());
        existingOffer.getUser().setFirstName(editedOfferDto.getUserFirstName());
        existingOffer.getUser().setLastName(editedOfferDto.getUserLastName());
        existingOffer.getUser().setCompany(editedOfferDto.getUserCompanyName());
        existingOffer.getUser().setEmailAddress(editedOfferDto.getUserEmailAddress());
        existingOffer.getUser().setTelephoneNumber(editedOfferDto.getUserTelephoneNumber());
        existingOffer.setOfferContent(editedOfferDto.getOfferContent());
        existingOffer.setServiceCategory(editedOfferDto.getServiceCategory());
        existingOffer.setDate(editedOfferDto.getDateTime());
        existingOffer.setExpiryDate(editedOfferDto.getExpiryDate());

        return offerRepository.save(existingOffer);
    }
}
