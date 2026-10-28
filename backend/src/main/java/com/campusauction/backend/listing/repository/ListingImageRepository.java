package com.campusauction.backend.listing.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusauction.backend.listing.entity.ListingImage;

public interface ListingImageRepository
        extends JpaRepository<ListingImage, Long> {

    List<ListingImage> findByListingIdOrderByCreatedAtAscIdAsc(
            Long listingId
    );

    long countByListingId(Long listingId);
}

