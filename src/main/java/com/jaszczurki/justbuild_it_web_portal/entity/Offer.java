package com.jaszczurki.justbuild_it_web_portal.entity;

import com.jaszczurki.justbuild_it_web_portal.entity.dictionary.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "offers")
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Offer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "offer_id", columnDefinition = "BINARY(16)")
    private UUID offerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_category")
    private ServiceCategory serviceCategory;

    @Column(name = "offer_content")
    private String offerContent;

    @Column(name = "city")
    private String city;

    @ManyToOne
    private User user;

    @Column(name = "date")
    private LocalDateTime date;

    private LocalDateTime expiryDate;

    public Offer() {
        this.offerId = null;
        this.serviceCategory = null;
        this.offerContent = "";
        this.city = "";
        this.user = new User();
        this.date = LocalDateTime.now();
        this.expiryDate = this.date.plusDays(30);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(offerId, offer.offerId) && serviceCategory == offer.serviceCategory && Objects.equals(offerContent, offer.offerContent) && Objects.equals(city, offer.city) && Objects.equals(user, offer.user) && Objects.equals(date, offer.date) && Objects.equals(expiryDate, offer.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId, serviceCategory, offerContent, city, user, date, expiryDate);
    }
}
