package com.campusauction.backend.listing.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.campusauction.backend.listing.entity.Listing;
import com.campusauction.backend.listing.entity.ListingImage;
import com.campusauction.backend.listing.repository.ListingImageRepository;
import com.campusauction.backend.listing.repository.ListingRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ListingImageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    private final ListingImageRepository imageRepository;
    private final ListingRepository listingRepository;
    private final Path uploadDirectory;
    private final int maxImagesPerListing;

    public ListingImageService(
            ListingImageRepository imageRepository,
            ListingRepository listingRepository,
            @Value("${app.upload.dir}") String uploadDirectory,
            @Value("${app.upload.max-images-per-listing:5}")
            int maxImagesPerListing
    ) {
        this.imageRepository = imageRepository;
        this.listingRepository = listingRepository;
        this.uploadDirectory = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();
        this.maxImagesPerListing = maxImagesPerListing;
    }

    @Transactional
    public ListingImage uploadImage(
            Long listingId,
            MultipartFile file
    ) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Listing not found: " + listingId
                        ));

        if (!"ACTIVE".equals(listing.getStatus())) {
            throw new IllegalArgumentException(
                    "Images can only be added to active listings"
            );
        }

        if (imageRepository.countByListingId(listingId)
                >= maxImagesPerListing) {
            throw new IllegalArgumentException(
                    "Maximum " + maxImagesPerListing
                            + " images allowed per listing"
            );
        }

        validateImage(file);

        String originalContentType = file.getContentType();
        String extension = getExtension(originalContentType);

        String filename = UUID.randomUUID() + extension;
        Path destination = uploadDirectory.resolve(filename).normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        try {
            Files.createDirectories(uploadDirectory);

            // Do not trust the client's filename.
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            String relativePath = "uploads/listings/" + filename;

            ListingImage image = new ListingImage(
                    listingId,
                    relativePath
            );

            return imageRepository.save(image);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not store the image",
                    exception
            );
        }
    }

    @Transactional(readOnly = true)
    public List<ListingImage> getImages(Long listingId) {
        if (!listingRepository.existsById(listingId)) {
            throw new IllegalArgumentException(
                    "Listing not found: " + listingId
            );
        }

        return imageRepository
                .findByListingIdOrderByCreatedAtAscIdAsc(listingId);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select an image"
            );
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException(
                    "Only JPEG, PNG, and WebP images are allowed"
            );
        }

        if (file.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Image size must not exceed 5 MB"
            );
        }
    }

    private String getExtension(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/webp":
                return ".webp";
            default:
                throw new IllegalArgumentException(
                        "Unsupported image type"
                );
        }
    }
}
