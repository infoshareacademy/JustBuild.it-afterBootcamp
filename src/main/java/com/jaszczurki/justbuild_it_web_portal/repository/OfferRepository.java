package com.jaszczurki.justbuild_it_web_portal.repository;

import com.jaszczurki.justbuild_it_web_portal.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
}
