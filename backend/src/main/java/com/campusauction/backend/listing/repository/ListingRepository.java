package com.campusauction.backend.listing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campusauction.backend.listing.entity.Listing;

public interface ListingRepository extends JpaRepository<Listing, Long> {

	List<Listing> findByStatusAndClosingAtAfterOrderByClosingAtAsc(String string, LocalDateTime now);

	List<Listing> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
	
			 @Query("""
				        SELECT l FROM Listing l
				        WHERE l.status = 'ACTIVE'
				          AND l.closingAt > :now
				          AND (:category IS NULL OR LOWER(l.category) = LOWER(:category))
				          AND (:search IS NULL OR
				               LOWER(l.title) LIKE LOWER(CONCAT('%', :search, '%')))
				        ORDER BY l.closingAt ASC
				        """)
		    List<Listing> browseListings(
		            @Param("now") LocalDateTime now,
		            @Param("category") String category,
		            @Param("search") String search
		    );

}
