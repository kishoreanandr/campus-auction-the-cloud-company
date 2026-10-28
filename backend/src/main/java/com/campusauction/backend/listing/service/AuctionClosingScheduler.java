package com.campusauction.backend.listing.service;


import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.campusauction.backend.listing.entity.Listing;
import com.campusauction.backend.listing.repository.ListingRepository;

@Component
public class AuctionClosingScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(AuctionClosingScheduler.class);

    private final ListingRepository listingRepository;
    private final ListingService listingService;

    public AuctionClosingScheduler(
            ListingRepository listingRepository,
            ListingService listingService) {
        this.listingRepository = listingRepository;
        this.listingService = listingService;
    }

    @Scheduled(fixedDelay = 10000)
    public void closeExpiredAuctions() {
        List<Listing> expiredListings =
                listingRepository.findByStatusAndClosingAtLessThanEqual(
                        "ACTIVE",
                        LocalDateTime.now()
                );

        for (Listing listing : expiredListings) {
            try {
                listingService.closeAuction(listing.getId());
                log.info("Processed expired auction: {}", listing.getId());
            } catch (Exception exception) {
                log.error(
                        "Failed to close auction: {}",
                        listing.getId(),
                        exception
                );
            }
        }
    }
}