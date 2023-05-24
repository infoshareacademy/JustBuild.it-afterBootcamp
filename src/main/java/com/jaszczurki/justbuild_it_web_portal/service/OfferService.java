package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.dto.OfferDto;
import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import com.jaszczurki.justbuild_it_web_portal.mapper.OfferMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferCreationService offerCreationService;
    private final OfferSearchingService offerSearchingService;
    private final OfferEditionService offerEditionService;
    private final UserOfferService userOfferService;
    private final UserAuthService userAuthService;
    private final OfferMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferService.class);

    public Offer addOffer(OfferDto offerDto) {
        Offer offer = mapper.fromDto(offerDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Long userId = userAuthService.findUserIdByUsername(username);
            User user = offer.getUser();
            user.setUserId(userId);
            offer.setUser(user);
        }
        return offerCreationService.saveOffer(offer);
    }

    public OfferDto provideNewOffer() {
        LOGGER.debug("Providing new offer DTO");
        return new OfferDto();
    }

    public List<OfferDto> provideNewFilteredOfferDtoList(String searchValue) {
        LOGGER.debug("Providing new filtered offer DTO list for search value: '{}'", searchValue);
        return mapper.toDtoList(offerSearchingService.findAllOffersFilteredBySearchValue(searchValue));
    }

    public List<OfferDto> provideNewFilteredByCategoryOfferDtoList(String category) {
        LOGGER.debug("Providing new filtered by category offer DTO list for category: '{}'", category);
        return mapper.toDtoList(offerSearchingService.findAllOffersFilteredByCategory(category));
    }

    public List<OfferDto> provideAllDtoList() {
        LOGGER.debug("Providing all offer DTO list");
        List<OfferDto> allOfferDtoList = mapper.toDtoList(offerSearchingService.findAllOffers());
        allOfferDtoList.sort(Comparator.comparing(OfferDto::getDateTime).reversed());
        return allOfferDtoList;
    }

    public List<OfferDto> provideOfferDtoList(String searchValue, String category) {
        List<OfferDto> filteredOfferDtoList;
        if (category != null && !category.isEmpty()) {
            filteredOfferDtoList = provideNewFilteredByCategoryOfferDtoList(category);
        } else {
            filteredOfferDtoList = provideNewFilteredOfferDtoList(searchValue);
        }
        return filteredOfferDtoList;
    }

    public OfferDto getOfferDtoById(UUID id) {
        LOGGER.debug("Getting offer DTO by ID: {}", id);
        OfferDto offerDtoById = offerEditionService.findOfferDtoById(id);
        offerDtoById.setDateTime(LocalDateTime.now());
        return offerDtoById;
    }

    public Offer updateOffer(OfferDto editedOfferDto) {
        LOGGER.debug("Updating offer DTO: '{}'", editedOfferDto);
        return offerEditionService.updateOffer(editedOfferDto);
    }

    public List<OfferDto> provideFilteredList(String searchValue, String category, int pageList, HttpSession session) {
        if (searchValue == null) {
            searchValue = "";
        }
        if (category == null) {
            category = "";
        }
        List<OfferDto> filteredDtoList;
        if (pageList == 1 || session.getAttribute("filteredOfferDtoList") == null) {
            filteredDtoList = provideOfferDtoList(searchValue, category);
            session.setAttribute("filteredOfferDtoList", filteredDtoList);
        } else {
            filteredDtoList = (List<OfferDto>) session.getAttribute("filteredOfferDtoList");
        }

        return filteredDtoList;
    }

    public Page<OfferDto> providePagination(Pageable pageable, List<OfferDto> allResources) {
        LOGGER.debug("Finding paginated offer DTO list with page: '{}' and size: '{}'", pageable.getPageNumber() + 1, pageable.getPageSize());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<OfferDto> offerDtos;

        LOGGER.debug("Size of allOfferDtoList: '{}'", allResources.size());

        if (allResources.isEmpty() || allResources.size() < startItem) {
            offerDtos = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allResources.size());
            offerDtos = allResources.subList(startItem, toIndex);
        }

        LOGGER.debug("Size of offerList: '{}'", offerDtos.size());

        return new PageImpl<>(offerDtos, pageable, allResources.size());
    }

    public List<Integer> calculatePageNumbers(Page<?> page) {
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            int maxVisiblePages = 5;
            int startPage = Math.max(1, page.getNumber() - (maxVisiblePages / 2));
            int endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
            return IntStream.rangeClosed(startPage, endPage)
                    .boxed()
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    public boolean checkOfferProlongability(OfferDto offerDto) {
        return offerDto.getExpiryDate().minusDays(3).isBefore(LocalDateTime.now());
    }

    public void prolongOffer(UUID id, int days) {
        OfferDto offerDtoById = offerEditionService.findOfferDtoById(id);
        if (days <= 30 && days >= 1) {
            LocalDateTime currentExpiryDate = offerDtoById.getExpiryDate();
            LocalDateTime newExpiryDate;
            if (LocalDateTime.now().isAfter(currentExpiryDate)) {
                newExpiryDate = LocalDateTime.now().plusDays(days);
            } else {
                newExpiryDate = currentExpiryDate.plusDays(days);
            }
            offerDtoById.setExpiryDate(newExpiryDate);
            offerEditionService.updateOffer(offerDtoById);
        } else {
            throw new IllegalArgumentException("Incorrect prolong period");
        }
    }

    public Long getUserIdByUsername(String username) {
        return userAuthService.findUserIdByUsername(username);
    }

    public List<OfferDto> provideUserOffers(Long userId) {
        LOGGER.debug("Providing user: {} offer DTO list", userId);
        return mapper.toDtoList(userOfferService.getUserOfferList(userId));
    }

    public List<OfferDto> provideActiveUserOffers(Long userId) {
        LOGGER.debug("Providing active offer DTO list for user: {}", userId);
        List<OfferDto> userOfferDtoList = provideUserOffers(userId);
        return userOfferDtoList.stream()
                .filter(offerDto -> userOfferService.isUserOfferActive(mapper.fromDto(offerDto)))
                .collect(Collectors.toList());
    }

    public List<OfferDto> provideInactiveUserOffers(Long userId) {
        LOGGER.debug("Providing inactive offer DTO list for user: {}", userId);
        List<OfferDto> userOfferDtoList = provideUserOffers(userId);
        return userOfferDtoList.stream()
                .filter(offerDto -> !userOfferService.isUserOfferActive(mapper.fromDto(offerDto)))
                .collect(Collectors.toList());
    }

    public void terminateOffer(UUID id) {
        OfferDto offerDtoById = offerEditionService.findOfferDtoById(id);
        offerDtoById.setExpiryDate(LocalDateTime.now());
        offerEditionService.updateOffer(offerDtoById);
    }
}
