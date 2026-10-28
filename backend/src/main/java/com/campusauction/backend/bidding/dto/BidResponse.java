package com.campusauction.backend.bidding.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.campusauction.backend.bidding.entity.Bid;

public class BidResponse {

    private Long id;
    private Long listingId;
    private Long bidderId;
    private String bidderName;
    private BigDecimal amount;
    private LocalDateTime bidTime;

    public BidResponse(
            Long id,
            Long listingId,
            Long bidderId,
            String bidderName,
            BigDecimal amount,
            LocalDateTime bidTime
    ) {
        this.id = id;
        this.listingId = listingId;
        this.bidderId = bidderId;
        this.bidderName = bidderName;
        this.amount = amount;
        this.bidTime = bidTime;
    }

    public static BidResponse from(
            Bid bid,
            String bidderName
    ) {
        return new BidResponse(
                bid.getId(),
                bid.getListingId(),
                bid.getBidderId(),
                bidderName,
                bid.getAmount(),
                bid.getBidTime()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getListingId() {
        return listingId;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }
}
