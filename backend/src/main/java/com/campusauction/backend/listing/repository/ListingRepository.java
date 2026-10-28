package com.campusauction.backend.listing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusauction.backend.listing.entity.Listing;

public interface ListingRepository extends JpaRepository<Listing, Long> {

	List<Listing> findByStatusAndClosingAtAfterOrderByClosingAtAsc(String string, LocalDateTime now);

	List<Listing> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

}
