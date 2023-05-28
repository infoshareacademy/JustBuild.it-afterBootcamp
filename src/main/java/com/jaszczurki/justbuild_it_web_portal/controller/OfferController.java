package com.jaszczurki.justbuild_it_web_portal.controller;

import com.jaszczurki.justbuild_it_web_portal.dto.OfferDto;
import com.jaszczurki.justbuild_it_web_portal.dto.ProlongRequest;
import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import com.jaszczurki.justbuild_it_web_portal.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferController.class);

    @GetMapping("/")
    public String goHome(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size) {
        List<OfferDto> allOfferDtoList = offerService.provideAllActiveOffersDtoList();
        Page<OfferDto> offerDtoPage = offerService.providePagination(PageRequest.of(page - 1, size), allOfferDtoList);
        List<Integer> pageNumbers = offerService.calculatePageNumbers(offerDtoPage);
        model.addAttribute("offerDtoPage", offerDtoPage);
        model.addAttribute("pageNumbers", pageNumbers);
        LOGGER.info("Returning home page with '{}' pages", pageNumbers.size());
        return "home";
    }

    @PostMapping("/prolong-ad")
    public void prolongAd(@RequestBody ProlongRequest request) {
        offerService.prolongOffer(request.getId(), request.getDays());
        LOGGER.debug("Prolonged offer '{} of '{}' days", request.getId(), request.getDays());
    }

    @GetMapping("/user/addOffer")
    public String goAdd(Model model) {
        model.addAttribute("offer", offerService.provideNewOffer());
        LOGGER.info("Opening addOffer page");
        return "addOffer";
    }

    @PostMapping("/user/addOffer")
    public String addOffer(@Valid @ModelAttribute("offer") OfferDto offerDto, BindingResult result) {
        if (result.hasErrors()) {
            LOGGER.debug("Binding Results has errors '{}' , returning addOffer page", result);
            return "addOffer";
        }
        offerService.addOffer(offerDto);
        LOGGER.info("New offer added with ID: {}", offerDto.getDtoOfferId());
        return "redirect:/";
    }

    @GetMapping("/searchOffer")
    public String goSearch(@RequestParam(required = false) String searchValue, @RequestParam(required = false) String category,
                           @RequestParam(defaultValue = "1") int pageList, @RequestParam(defaultValue = "8") int sizeList,
                           HttpSession session, Model model) {
        LOGGER.info("Searching for offers with searchValue: '{}' and category: '{}'", searchValue, category);
        List<OfferDto> filteredOfferDtoList = offerService.provideFilteredList(searchValue, category, pageList, session);
        Page<OfferDto> offerDtoListPage = offerService.providePagination(PageRequest.of(pageList - 1, sizeList), filteredOfferDtoList);
        model.addAttribute("filteredOfferDtoList", offerDtoListPage);
        model.addAttribute("pagesDtoList", offerService.calculatePageNumbers(offerDtoListPage));
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("category", category);
        LOGGER.info("Returning searchOffer page with '{}' offers found", filteredOfferDtoList.size());
        return "searchOffer";
    }

    @GetMapping("editOffer/{id}")
    @PreAuthorize("isAuthenticated()")
    public String goEdit(@PathVariable UUID id, Model model) {
        OfferDto offerDto = offerService.getOfferDtoById(id);

        if (offerService.isLoggedUserAdminOrOwnerForOffer(offerDto)) {
            boolean prolongable = offerService.checkOfferProlongability(offerDto);
            boolean expired = LocalDateTime.now().isAfter(offerDto.getExpiryDate());
            model.addAttribute("offer", offerDto);
            model.addAttribute("prolongable", prolongable);
            model.addAttribute("expired", expired);
            LOGGER.info("Opening editOffer page for offer with ID: '{}'", id);
            return "editOffer";
        } else {
            LOGGER.info("Access Denied for offer: '{}'", id);
            return "loginForm";
        }
    }

    @PostMapping("editOffer")
    @PreAuthorize("isAuthenticated()")
    public String editOffer(@Valid @ModelAttribute("offer") OfferDto offerDto, BindingResult result) {
        if (result.hasErrors()) {
            LOGGER.debug("Binding Results has errors '{}' , returning addOffer page", result);
            return "editOffer";
        }
        offerService.updateOffer(offerDto);
        LOGGER.debug("Updated offer with ID: {}", offerDto.getDtoOfferId());
        return "redirect:/";
    }

    @GetMapping("myOffers")
    @PreAuthorize("isAuthenticated() and !principal.equals('anonymousUser')")
    public String showUserOffers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        System.out.println(isAdmin);

        String username = authentication.getName();
        Long userId = offerService.getUserIdByUsername(username);
        LOGGER.info("Searching offers for user: '{}'", userId);

        List<OfferDto> userActiveOffers;
        List<OfferDto> userInactiveOffers;
        if (username.equals("admin")) {
            userActiveOffers = offerService.provideAllActiveOffersDtoList();
            userInactiveOffers = offerService.provideAllInactiveOffersDtoList();
        } else {
            userActiveOffers = offerService.provideActiveUserOffers(userId);
            userInactiveOffers = offerService.provideInactiveUserOffers(userId);
        }
        model.addAttribute("activeOffers", userActiveOffers);
        model.addAttribute("inactiveOffers", userInactiveOffers);
        LOGGER.debug("Returning user: '{}' Offers list page with '{}' active offers found",userId, userActiveOffers.size());
        LOGGER.debug("Returning user: '{}' Offers list page with '{}' inactive offers found",userId, userInactiveOffers.size());
        return "myOffers";
    }

    @GetMapping("/terminateOffer/{id}")
    public ResponseEntity<Offer> terminateOffer(@PathVariable(value = "id") UUID id) {
        offerService.terminateOffer(id);
        LOGGER.info("Terminated offer: '{}'", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
