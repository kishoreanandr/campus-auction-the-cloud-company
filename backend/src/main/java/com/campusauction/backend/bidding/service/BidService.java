package com.campusauction.backend.bidding.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusauction.backend.bidding.dto.BidRequest;
import com.campusauction.backend.bidding.dto.BidResponse;
import com.campusauction.backend.bidding.entity.Bid;
import com.campusauction.backend.bidding.repository.BidRepository;
import com.campusauction.backend.listing.entity.Listing;
import com.campusauction.backend.listing.repository.ListingRepository;
import com.campusauction.backend.user.entity.User;
import com.campusauction.backend.user.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public BidService(
            BidRepository bidRepository,
            ListingRepository listingRepository,
            UserRepository userRepository
    ) {
        this.bidRepository = bidRepository;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BidResponse placeBid(Long listingId, Long bidderId, BidRequest request) {

        // 1. Find the auction listing.
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Listing not found: " + listingId
                        ));

        // 2. Check whether the auction is open.
        LocalDateTime now = LocalDateTime.now();

        if (!"ACTIVE".equals(listing.getStatus())) {
            throw new IllegalArgumentException(
                    "This listing is not active"
            );
        }

        if (!listing.getClosingAt().isAfter(now)) {
            throw new IllegalArgumentException(
                    "The auction has already closed"
            );
        }

        // 3. Find the bidder.
        User bidder = userRepository.findById(bidderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bidder not found: " + bidderId
                        ));

        // 4. A seller cannot bid on their own listing.
        if (listing.getSellerId().equals(bidder.getId())) {
            throw new IllegalArgumentException(
                    "You cannot bid on your own listing"
            );
        }

        BigDecimal amount = request.getAmount();

        // 5. Calculate the minimum amount allowed.
        BigDecimal minimumAllowed;

        if (listing.getCurrentHighestBid() == null) {
            // First bid must meet or exceed the starting price.
            minimumAllowed = listing.getStartingPrice();
        } else {
            // Later bids must meet or exceed the current price
            // plus the minimum increment.
            minimumAllowed = listing.getCurrentHighestBid()
                    .add(listing.getMinimumIncrement());
        }

        if (amount.compareTo(minimumAllowed) < 0) {
            throw new IllegalArgumentException(
                    "Bid must be at least " + minimumAllowed
            );
        }

        // 6. Create a new bid record.
        Bid bid = new Bid(
                listing.getId(),
                bidder.getId(),
                amount
        );

        Bid savedBid = bidRepository.saveAndFlush(bid);

        // 7. Update the listing's current highest bid.
        listing.setCurrentHighestBid(amount);
        listingRepository.saveAndFlush(listing);

        // 8. Return the saved bid with the bidder's public name.
        return BidResponse.from(savedBid, bidder.getName());
    }

    @Transactional(readOnly = true)
    public List<BidResponse> getBidHistory(Long listingId) {

        // Ensure the listing exists, even if it has no bids.
        if (!listingRepository.existsById(listingId)) {
            throw new IllegalArgumentException(
                    "Listing not found: " + listingId
            );
        }

        List<Bid> bids =
                bidRepository.findByListingIdOrderByBidTimeDescIdDesc(
                        listingId
                );

        return bids.stream()
                .map(bid -> {
                    User bidder = userRepository.findById(bid.getBidderId())
                            .orElseThrow(() ->
                                    new IllegalStateException(
                                            "Bidder not found for bid: "
                                                    + bid.getId()
                                    ));

                    return BidResponse.from(bid, bidder.getName());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public BidResponse getHighestBid(Long listingId) {

        if (!listingRepository.existsById(listingId)) {
            throw new IllegalArgumentException(
                    "Listing not found: " + listingId
            );
        }

        return bidRepository
                .findFirstByListingIdOrderByAmountDescBidTimeAscIdAsc(
                        listingId
                )
                .map(bid -> {
                    User bidder = userRepository.findById(bid.getBidderId())
                            .orElseThrow(() ->
                                    new IllegalStateException(
                                            "Bidder not found for bid: "
                                                    + bid.getId()
                                    ));

                    return BidResponse.from(bid, bidder.getName());
                })
                .orElse(null);
    }
}
