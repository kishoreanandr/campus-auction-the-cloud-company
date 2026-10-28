package com.campusauction.backend.listing.dto;



import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListingRequest {

    @NotNull(message = "Seller ID is required during development")
    @Positive(message = "Seller ID must be positive")
    private Long sellerId;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Category is required")
    @Size(max = 40, message = "Category cannot exceed 40 characters")
    private String category;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Condition is required")
    @Size(max = 30, message = "Condition cannot exceed 30 characters")
    private String itemCondition;

    @NotNull(message = "Starting price is required")
    @DecimalMin(value = "0.01", message = "Starting price must be greater than zero")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal startingPrice;

    @NotNull(message = "Minimum increment is required")
    @DecimalMin(value = "0.01", message = "Minimum increment must be greater than zero")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal minimumIncrement;

    @NotNull(message = "Closing date and time are required")
    private LocalDateTime closingAt;

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

	public LocalDateTime getClosingAt() {
		return closingAt;
	}

	public void setClosingAt(LocalDateTime closingAt) {
		this.closingAt = closingAt;
	}
}
