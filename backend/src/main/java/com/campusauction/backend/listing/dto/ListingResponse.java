package com.campusauction.backend.listing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingResponse {
    private Long id;
    private Long sellerId;
    private String title;
    private String category;
    private String description;
    private String itemCondition;
    private BigDecimal startingPrice;
    private BigDecimal minimumIncrement;
    private BigDecimal currentHighestBid;
    private LocalDateTime closingAt;
    private String status;
    private LocalDateTime createdAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getItemCondition() {
		return itemCondition;
	}
	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}
	public BigDecimal getStartingPrice() {
		return startingPrice;
	}
	public void setStartingPrice(BigDecimal startingPrice) {
		this.startingPrice = startingPrice;
	}
	public BigDecimal getMinimumIncrement() {
		return minimumIncrement;
	}
	public void setMinimumIncrement(BigDecimal minimumIncrement) {
		this.minimumIncrement = minimumIncrement;
	}
	public BigDecimal getCurrentHighestBid() {
		return currentHighestBid;
	}
	public void setCurrentHighestBid(BigDecimal currentHighestBid) {
		this.currentHighestBid = currentHighestBid;
	}
	public LocalDateTime getClosingAt() {
		return closingAt;
	}
	public void setClosingAt(LocalDateTime closingAt) {
		this.closingAt = closingAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

