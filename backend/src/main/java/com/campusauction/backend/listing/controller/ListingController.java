package com.campusauction.backend.listing.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.campusauction.backend.listing.dto.ListingRequest;
import com.campusauction.backend.listing.dto.ListingResponse;
import com.campusauction.backend.listing.exception.ListingNotFoundException;
import com.campusauction.backend.listing.exception.ListingRuleException;
import com.campusauction.backend.listing.service.ListingService;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping
    public ResponseEntity<ListingResponse> createListing(
            @Valid @RequestBody ListingRequest request
    ) throws ListingRuleException {
        return new ResponseEntity<ListingResponse>(listingService.createListing(request),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ListingResponse>> getActiveListings() {
        return new ResponseEntity<List<ListingResponse>>(listingService.getActiveListings(),HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ListingResponse>> getSellerListings(
            @RequestParam Long sellerId
    ) throws ListingRuleException {
        return new ResponseEntity<List<ListingResponse>>(listingService.getSellerListings(sellerId),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getListing(@PathVariable Long id) throws ListingNotFoundException {
        return new ResponseEntity<ListingResponse>(listingService.getListingById(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingResponse> updateListing(
            @PathVariable Long id,
            @Valid @RequestBody ListingRequest request
    ) throws ListingRuleException, ListingNotFoundException {
        return new ResponseEntity<ListingResponse>(listingService.updateListing(id, request),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelListing(
            @PathVariable Long id,
            @RequestParam Long sellerId
    ) throws ListingNotFoundException, ListingRuleException {
        listingService.cancelListing(id, sellerId);
    }
}
