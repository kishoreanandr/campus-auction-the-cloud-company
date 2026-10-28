package com.campusauction.backend.bidding.controller;



import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.campusauction.backend.bidding.dto.BidRequest;
import com.campusauction.backend.bidding.dto.BidResponse;
import com.campusauction.backend.bidding.service.BidService;
import com.campusauction.backend.security.AuthPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/listings/{listingId}/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // Place a bid
    @PostMapping
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable Long listingId,
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody BidRequest request
    ) {
        BidResponse response = bidService.placeBid(
                listingId,
                principal.getUserId(),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get public bid history
    @GetMapping
    public ResponseEntity<List<BidResponse>> getBidHistory(
            @PathVariable Long listingId
    ) {
        return ResponseEntity.ok(
                bidService.getBidHistory(listingId)
        );
    }

    // Get the highest bid
    @GetMapping("/highest")
    public ResponseEntity<BidResponse> getHighestBid(
            @PathVariable Long listingId
    ) {
        BidResponse response = bidService.getHighestBid(listingId);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
