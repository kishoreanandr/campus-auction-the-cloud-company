package com.campusauction.backend.listing.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.campusauction.backend.listing.entity.ListingImage;
import com.campusauction.backend.listing.service.ListingImageService;

import java.util.List;

@RestController
@RequestMapping("/api/listings/{listingId}/images")
public class ListingImageController {

    private final ListingImageService listingImageService;

    public ListingImageController(
            ListingImageService listingImageService
    ) {
        this.listingImageService = listingImageService;
    }

    // Upload one image for a listing.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListingImage> uploadImage(
            @PathVariable Long listingId,
            @RequestParam("file") MultipartFile file
    ) {
        ListingImage image =
                listingImageService.uploadImage(listingId, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(image);
    }

    // Get all image records for a listing.
    @GetMapping
    public ResponseEntity<List<ListingImage>> getImages(
            @PathVariable Long listingId
    ) {
        return ResponseEntity.ok(
                listingImageService.getImages(listingId)
        );
    }
}
