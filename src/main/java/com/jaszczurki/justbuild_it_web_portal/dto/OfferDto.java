package com.jaszczurki.justbuild_it_web_portal.dto;

import com.jaszczurki.justbuild_it_web_portal.entity.dictionary.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.jaszczurki.justbuild_it_web_portal.entity.constants.ApplicationConstants.DISPLAY_DATE_FORMAT;
import static com.jaszczurki.justbuild_it_web_portal.entity.constants.ApplicationConstants.EMAIL_FORMAT;
import static com.jaszczurki.justbuild_it_web_portal.entity.constants.ApplicationConstants.EXPIRY_DATE_FORMAT;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OfferDto {

    private UUID dtoOfferId;
    private ServiceCategory serviceCategory;
    private String offerContent;
    @NotBlank
    private String city;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    @NotBlank
    private String userCompanyName;
    @Email(regexp = EMAIL_FORMAT)
    @NotBlank
    private String userEmailAddress;
    @NotBlank
    private String userTelephoneNumber;
    @DateTimeFormat(pattern = DISPLAY_DATE_FORMAT)
    private LocalDateTime dateTime;

    @DateTimeFormat(pattern = EXPIRY_DATE_FORMAT)
    private LocalDateTime expiryDate;

    public OfferDto() {
        this.dtoOfferId = null;
        this.serviceCategory = null;
        this.offerContent = "";
        this.city = "";
        this.userFirstName = "";
        this.userLastName = "";
        this.userCompanyName = "";
        this.userEmailAddress = "";
        this.userTelephoneNumber = "";
        this.dateTime = LocalDateTime.now();
        this.expiryDate = dateTime.plusDays(30);
    }
}
