package com.campusauction.backend.listing.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auction_listings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 40)
    private String category;

    @Column
    private String description;

    @Column(name = "item_condition", nullable = false, length = 30)
    private String itemCondition;

    @Column(name = "starting_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal startingPrice;

    @Column(name = "minimum_increment", nullable = false, precision = 12, scale = 2)
    private BigDecimal minimumIncrement;

    @Column(name = "current_highest_bid", precision = 12, scale = 2)
    private BigDecimal currentHighestBid;

    @Column(name = "closing_at", nullable = false)
    private LocalDateTime closingAt;

    @Column(nullable = false, length = 30)
    private String status;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
