package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.jaszczurki.justbuild_it_web_portal.entity.constants.ApplicationConstants.DB_REFERENCE;

@Service
@RequiredArgsConstructor
class OfferSearchingService {

    private final OfferRepository offerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferSearchingService.class);

    public List<Offer> findAllOffers() {
        LOGGER.debug("Retrieving all offers from db: {}", DB_REFERENCE);
        return offerRepository.findAll();
    }

    public List<Offer> findAllOffersFilteredBySearchValue(String searchValue) {
        LOGGER.debug("Retrieving offers from db: {} filtered by search value: '{}'", DB_REFERENCE, searchValue);
        List<Offer> offers = offerRepository.findAll();

        return offers.stream()
                .sorted(Comparator.comparing(Offer::getDate).reversed())
                .filter(value -> value.getOfferContent().toLowerCase().contains(searchValue.toLowerCase())
                        || value.getOfferId().toString().contains(searchValue)
                        || value.getCity().toLowerCase().contains(searchValue.toLowerCase())
                        || value.getUser().getCompany().toLowerCase().contains(searchValue.toLowerCase())
                        || value.getUser().getLastName().toLowerCase().contains(searchValue.toLowerCase())
                        || value.getServiceCategory().toString().toLowerCase().contains(searchValue.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Offer> findAllOffersFilteredByCategory(String category) {
        LOGGER.debug("Retrieving offers from file {} filtered by category '{}'", DB_REFERENCE, category);
        List<Offer> offers = offerRepository.findAll();

        return offers.stream()
                .sorted(Comparator.comparing(Offer::getDate).reversed())
                .filter(value -> value.getServiceCategory().toString().equals(category))
                .collect(Collectors.toList());
    }
}
