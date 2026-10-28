package com.campusauction.backend.listing.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.campusauction.backend.bidding.entity.Bid;
import com.campusauction.backend.bidding.repository.BidRepository;
import com.campusauction.backend.listing.dto.ListingRequest;
import com.campusauction.backend.listing.dto.ListingResponse;
import com.campusauction.backend.listing.entity.Listing;
import com.campusauction.backend.listing.exception.ListingNotFoundException;
import com.campusauction.backend.listing.exception.ListingRuleException;
import com.campusauction.backend.listing.repository.ListingRepository;
import com.campusauction.backend.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ListingService {
	 	private final ListingRepository listingRepository;
	    private final UserRepository userRepository;
	    private final BidRepository bidRepository;

	    public ListingService(
	            ListingRepository listingRepository,
	            UserRepository userRepository,
	            BidRepository bidRepository) {
	        this.listingRepository = listingRepository;
	        this.userRepository = userRepository;
	        this.bidRepository=bidRepository;
	    }
	    
	    @Transactional
	    public ListingResponse createListing(ListingRequest request) throws ListingRuleException {
	        validateClosingTime(request.getClosingAt());

	        if (!userRepository.existsById(request.getSellerId())) {
	            throw new ListingRuleException(
	                    "Seller does not exist. Use an existing user ID."
	            );
	        }

	        Listing listing = new Listing();
	        copyRequestToListing(request, listing);

	        listing.setSellerId(request.getSellerId());
	        listing.setStatus("ACTIVE");

	        return convertToListingResponse(listingRepository.save(listing));
	    }
	    
	    @Transactional
	    public List<ListingResponse> getActiveListings() {
	        return listingRepository
	                .findByStatusAndClosingAtAfterOrderByClosingAtAsc(
	                        "ACTIVE",
	                        LocalDateTime.now()
	                )
	                .stream()
	                .map(this::convertToListingResponse)
	                .toList();
	    }
	    
	    @Transactional
	    public ListingResponse getListingById(Long id) throws ListingNotFoundException {
	        Listing listing = findListing(id);
	        return convertToListingResponse(listing);
	    } 
	    
	    @Transactional
	    public List<ListingResponse> getSellerListings(Long sellerId) throws ListingRuleException {
	        if (!userRepository.existsById(sellerId)) {
	            throw new ListingRuleException("Seller does not exist.");
	        }

	        return listingRepository
	                .findBySellerIdOrderByCreatedAtDesc(sellerId)
	                .stream()
	                .map(this::convertToListingResponse)
	                .toList();
	    }
	    
	    @Transactional
	    public ListingResponse updateListing(
	            Long id,
	            ListingRequest request
	    ) throws ListingRuleException, ListingNotFoundException {
	        Listing listing = findListing(id);

	        checkOwnership(listing, request.getSellerId());
	        checkCanBeChanged(listing);
	        validateClosingTime(request.getClosingAt());

	        copyRequestToListing(request, listing);

	        return convertToListingResponse(listingRepository.save(listing));
	    }
	    

		@Transactional
		public List<ListingResponse> browseListings(
		        String category,
		        String search
		) {
		    String normalizedCategory = normalizeFilter(category);
		    String normalizedSearch = normalizeFilter(search);
		
		    return listingRepository.browseListings(
		                    LocalDateTime.now(),
		                    normalizedCategory,
		                    normalizedSearch
		            )
		            .stream()
		            .map(this::convertToListingResponse)
		            .toList();
		}
		
		private String normalizeFilter(String value) {
		    if (value == null || value.isBlank()) {
		        return null;
		    }
		
		    return value.trim();
		}

	    
	    
	    private void checkCanBeChanged(Listing listing) throws ListingRuleException {
			// TODO Auto-generated method stub
	    	 if (!"ACTIVE".equals(listing.getStatus())) {
	             throw new ListingRuleException(
	                     "Only active listings can be edited or cancelled."
	             );
	         }

	         if (!listing.getClosingAt().isAfter(LocalDateTime.now())) {
	             throw new ListingRuleException(
	                     "This auction has already closed."
	             );
	         }

	         if (listing.getCurrentHighestBid() != null) {
	             throw new ListingRuleException(
	                     "A listing cannot be edited or cancelled after bidding begins."
	             );
	         }
			
		}

		private void checkOwnership(Listing listing, Long sellerId) throws ListingRuleException {
			// TODO Auto-generated method stub
			if (sellerId == null || !listing.getSellerId().equals(sellerId)) {
	            throw new ListingRuleException(
	                    "You can only manage your own listings."
	            );
	        }
		}

		private Listing findListing(Long id) throws ListingNotFoundException {
			// TODO Auto-generated method stub
	    	return listingRepository.findById(id)
	                .orElseThrow(() -> new ListingNotFoundException(id+": id not Found"));		}

		private ListingResponse convertToListingResponse(Listing listing) {
			// TODO Auto-generated method stub
	    	ListingResponse response=new ListingResponse();
	    	response.setCategory(listing.getCategory());
	    	response.setClosingAt(listing.getClosingAt());
	    	response.setCreatedAt(listing.getCreatedAt());
	    	response.setCurrentHighestBid(listing.getCurrentHighestBid());
	    	response.setDescription(listing.getDescription());
	    	response.setId(listing.getId());
	    	response.setItemCondition(listing.getItemCondition());
	    	response.setMinimumIncrement(listing.getMinimumIncrement());
	    	response.setSellerId(listing.getSellerId());
	    	response.setStartingPrice(listing.getStartingPrice());
	    	response.setStatus(listing.getStatus());
	    	response.setTitle(listing.getTitle());
			return response;
		}

		private void copyRequestToListing(ListingRequest request, Listing listing) {
			// TODO Auto-generated method stub
	    	 listing.setTitle(request.getTitle().trim());
	         listing.setCategory(request.getCategory().trim());
	         listing.setDescription(request.getDescription().trim());
	         listing.setItemCondition(request.getItemCondition().trim());
	         listing.setStartingPrice(request.getStartingPrice());
	         listing.setMinimumIncrement(request.getMinimumIncrement());
	         listing.setClosingAt(request.getClosingAt());
			
		}

		private void validateClosingTime(LocalDateTime closingAt) throws ListingRuleException {
	        if (closingAt == null || !closingAt.isAfter(LocalDateTime.now())) {
	            throw new ListingRuleException(
	                    "Closing date and time must be in the future."
	            );
	        }
	    }

		 @Transactional
	    public void cancelListing(Long id, Long sellerId) throws ListingNotFoundException, ListingRuleException {
	        Listing listing = findListing(id);
	
	        checkOwnership(listing, sellerId);
	        checkCanBeChanged(listing);
	
	        listing.setStatus("CANCELLED");
	        listingRepository.save(listing);
	    }
		 
		 @Transactional
		    public ListingResponse closeAuction(Long listingId)
		            throws ListingNotFoundException, ListingRuleException {

		        Listing listing = findListing(listingId);

		        if (!"ACTIVE".equals(listing.getStatus())) {
		            throw new ListingRuleException(
		                    "Only active auctions can be closed."
		            );
		        }

		        LocalDateTime now = LocalDateTime.now();

		        if (listing.getClosingAt().isAfter(now)) {
		            throw new ListingRuleException(
		                    "The auction closing time has not been reached."
		            );
		        }

		        var highestBid = bidRepository
		                .findFirstByListingIdOrderByAmountDescBidTimeAscIdAsc(
		                        listingId
		                );

		        if (highestBid.isEmpty()) {
		            listing.setStatus("UNSOLD");
		            listing.setCurrentHighestBid(null);
		        } else {
		            Bid winningBid = highestBid.get();

		            listing.setStatus("PENDING_SELLER_APPROVAL");
		            listing.setCurrentHighestBid(winningBid.getAmount());
		        }

		        Listing savedListing = listingRepository.save(listing);

		        return convertToListingResponse(savedListing);
		    }

}
