package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OfferCreationService {

    private final OfferRepository offerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferCreationService.class);

    public Offer saveOffer(Offer offer) {
        LOGGER.debug("Adding offer: '{}'", offer.getOfferId());
        return offerRepository.save(offer);
    }
}
