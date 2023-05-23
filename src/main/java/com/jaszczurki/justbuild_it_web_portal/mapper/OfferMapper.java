package com.jaszczurki.justbuild_it_web_portal.mapper;

import com.jaszczurki.justbuild_it_web_portal.dto.OfferDto;
import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferMapper {

    public OfferDto toDto(Offer offer) {
        return OfferDto.builder()
                .dtoOfferId(offer.getOfferId())
                .serviceCategory(offer.getServiceCategory())
                .offerContent(offer.getOfferContent())
                .city(offer.getCity())
                .dateTime(offer.getDate())
                .expiryDate(offer.getExpiryDate())
                .userId(offer.getUser().getUserId())
                .userFirstName(offer.getUser().getFirstName())
                .userLastName(offer.getUser().getLastName())
                .userCompanyName(offer.getUser().getCompany())
                .userEmailAddress(offer.getUser().getEmailAddress())
                .userTelephoneNumber(offer.getUser().getTelephoneNumber())
                .build();
    }

    public Offer fromDto(OfferDto dto) {
        return Offer.builder()
                .offerId(dto.getDtoOfferId())
                .serviceCategory(dto.getServiceCategory())
                .offerContent(dto.getOfferContent())
                .city(dto.getCity())
                .date(dto.getDateTime())
                .expiryDate(dto.getExpiryDate())
                .user(User.builder()
                        .userId(dto.getUserId())
                        .firstName(dto.getUserFirstName())
                        .lastName(dto.getUserLastName())
                        .company(dto.getUserCompanyName())
                        .emailAddress(dto.getUserEmailAddress())
                        .telephoneNumber(dto.getUserTelephoneNumber())
                        .build())
                .build();
    }

    public List<OfferDto> toDtoList(List<Offer> offerList) {
        return offerList.stream()
                .map(offer -> new OfferDto(offer.getOfferId(), offer.getServiceCategory(), offer.getOfferContent(),
                        offer.getCity(), offer.getUser().getUserId(), offer.getUser().getFirstName(), offer.getUser().getLastName(),
                        offer.getUser().getCompany(), offer.getUser().getEmailAddress(),
                        offer.getUser().getTelephoneNumber(), offer.getDate(), offer.getExpiryDate()))
                .collect(Collectors.toList());
    }
}
