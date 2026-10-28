package com.campusauction.backend.listing.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listing_images")
public class ListingImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @Column(name = "image_path", nullable = false, length = 500)
    private String imagePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ListingImage() {
    }

    public ListingImage(Long listingId, String imagePath) {
        this.listingId = listingId;
        this.imagePath = imagePath;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getListingId() {
        return listingId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
