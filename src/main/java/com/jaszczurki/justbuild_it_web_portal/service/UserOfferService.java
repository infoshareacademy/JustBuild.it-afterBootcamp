package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserOfferService {

    private final OfferRepository offerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOfferService.class);

    public List<Offer> getUserOfferList(Long userId) {
        LOGGER.debug("Getting user offer list for user: '{}'", userId);
        List<Offer> userOffers = offerRepository.findAll();
        return userOffers.stream()
                .filter(offer -> userId.equals(offer.getUser().getUserId()))
                .collect(Collectors.toList());
    }

    public boolean isUserOfferActive(Offer offer) {
        LOGGER.debug("Checking if offer is active: '{}'", offer);
        return offer.getExpiryDate().isAfter(LocalDateTime.now());
    }
}

