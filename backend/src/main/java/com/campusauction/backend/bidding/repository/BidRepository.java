package com.campusauction.backend.bidding.repository;



import org.springframework.data.jpa.repository.JpaRepository;


import com.campusauction.backend.bidding.entity.Bid;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    // Public bid history: newest bid first
    List<Bid> findByListingIdOrderByBidTimeDescIdDesc(Long listingId);

    // Find the highest accepted bid for a listing
    Optional<Bid> findFirstByListingIdOrderByAmountDescBidTimeAscIdAsc(
            Long listingId
    );

    // Check whether a student has already bid on a listing
    boolean existsByListingIdAndBidderId(Long listingId, Long bidderId);
}
