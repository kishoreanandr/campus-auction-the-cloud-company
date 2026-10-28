package com.campusauction.backend.bidding.dto;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class BidRequest {

    @NotNull(message = "Bidder ID is required")
    @Positive(message = "Bidder ID must be positive")
    private Long bidderId;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "0.01", message = "Bid amount must be greater than zero")
    @Digits(integer = 10, fraction = 2,
            message = "Bid amount can have at most 2 decimal places")
    private BigDecimal amount;

    public BidRequest() {
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
