package com.campusauction.backend.bidding.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @Column(name = "bidder_id", nullable = false)
    private Long bidderId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime;

    protected Bid() {
    }

    public Bid(Long listingId, Long bidderId, BigDecimal amount) {
        this.listingId = listingId;
        this.bidderId = bidderId;
        this.amount = amount;
    }

    @PrePersist
    protected void onCreate() {
        if (bidTime == null) {
            bidTime = LocalDateTime.now();
        }
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

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }
}