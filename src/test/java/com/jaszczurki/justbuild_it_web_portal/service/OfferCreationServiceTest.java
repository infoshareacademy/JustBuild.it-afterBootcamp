package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import com.jaszczurki.justbuild_it_web_portal.entity.dictionary.ServiceCategory;
import com.jaszczurki.justbuild_it_web_portal.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OfferCreationServiceTest {

    private final static LocalDateTime NOW = LocalDateTime.now();

    private OfferRepository offerRepositoryMock;
    private OfferCreationService offerCreationService;

    @BeforeEach
    void setUp() {
        offerRepositoryMock = mock(OfferRepository.class);
        offerCreationService = new OfferCreationService(offerRepositoryMock);
    }

    @Test
    void testIfOfferIsCorrectlyAdded() {
        // given
        Offer offer = createOffer;

        // when
        offerCreationService.saveOffer(offer);

        // then
        ArgumentCaptor<Offer> offerCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepositoryMock).save(offerCaptor.capture());
        Offer savedOffer = offerCaptor.getValue();

        assertThat(savedOffer.getOfferId()).isEqualTo(offer.getOfferId());
        assertThat(savedOffer.getCity()).isEqualTo(offer.getCity());
        assertThat(savedOffer.getOfferContent()).isEqualTo(offer.getOfferContent());
        assertThat(savedOffer.getServiceCategory()).isEqualTo(offer.getServiceCategory());
        assertThat(savedOffer.getExpiryDate()).isEqualTo(offer.getExpiryDate());
        assertThat(savedOffer.getUser().getUserId()).isEqualTo(offer.getUser().getUserId());
    }

    Offer createOffer = Offer.builder()
            .offerId(UUID.randomUUID())
            .serviceCategory(ServiceCategory.CONSTRUCTION)
            .offerContent("some content")
            .city("Warsaw")
            .date(NOW)
            .expiryDate(NOW.plusDays(30L))
            .user(User.builder()
                    .userId(1L)
                    .build())
            .build();
}