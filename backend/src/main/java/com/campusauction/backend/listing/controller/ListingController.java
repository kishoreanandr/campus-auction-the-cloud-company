package com.campusauction.backend.listing.controller;


import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.campusauction.backend.listing.dto.ListingRequest;
import com.campusauction.backend.listing.dto.ListingResponse;
import com.campusauction.backend.listing.exception.ListingNotFoundException;
import com.campusauction.backend.listing.exception.ListingRuleException;
import com.campusauction.backend.listing.service.ListingService;
import com.campusauction.backend.security.AuthPrincipal;

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
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody ListingRequest request
    ) throws ListingRuleException {
        return new ResponseEntity<ListingResponse>(
                listingService.createListing(principal.getUserId(), request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<ListingResponse>> getActiveListings() {
        return new ResponseEntity<List<ListingResponse>>(listingService.getActiveListings(),HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ListingResponse>> getSellerListings(
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws ListingRuleException {
        return new ResponseEntity<List<ListingResponse>>(
                listingService.getSellerListings(principal.getUserId()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getListing(@PathVariable Long id) throws ListingNotFoundException {
        return new ResponseEntity<ListingResponse>(listingService.getListingById(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingResponse> updateListing(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody ListingRequest request
    ) throws ListingRuleException, ListingNotFoundException {
        return new ResponseEntity<ListingResponse>(
                listingService.updateListing(id, request, principal.getUserId()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelListing(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws ListingNotFoundException, ListingRuleException {
        listingService.cancelListing(id, principal.getUserId());
    }
    
    @GetMapping("/browse")
    public List<ListingResponse> browseListings(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        return listingService.browseListings(category, search);
    }
    
    @PostMapping("/{id}/close")
    public ResponseEntity<ListingResponse> closeAuction(
            @PathVariable Long id) throws ListingNotFoundException,
                                        ListingRuleException {

        ListingResponse response = listingService.closeAuction(id);

        return ResponseEntity.ok(response);
    }

@PostMapping("/{id}/approve")
public ResponseEntity<ListingResponse> approveWinningBid(
        @PathVariable Long id,
        @AuthenticationPrincipal AuthPrincipal principal)
        throws ListingNotFoundException, ListingRuleException {

    return ResponseEntity.ok(
            listingService.approveWinningBid(id, principal.getUserId())
    );
}
	
	@PostMapping("/{id}/reject")
	public ResponseEntity<ListingResponse> rejectWinningBid(
	        @PathVariable Long id,
	        @AuthenticationPrincipal AuthPrincipal principal)
	        throws ListingNotFoundException, ListingRuleException {
	
	    return ResponseEntity.ok(
	            listingService.rejectWinningBid(id, principal.getUserId())
	    );
	}

}
