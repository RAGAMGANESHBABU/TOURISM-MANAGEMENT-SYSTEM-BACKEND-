package com.tourism.management.system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tourism.management.system.model.HomestayListing;
import com.tourism.management.system.repository.HomestayRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:3000")
public class HomestayController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final HomestayRepository repository;

    public HomestayController(HomestayRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<HomestayListing> getAllListings() {
        return repository.findAll();
    }

    @PostMapping
    public HomestayListing addListing(@RequestParam("title") String title,
                                      @RequestParam("location") String location,
                                      @RequestParam("price") Double price,
                                      @RequestParam("image") MultipartFile image) throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file to the upload directory
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        // Save listing details
        HomestayListing listing = new HomestayListing();
        listing.setTitle(title);
        listing.setLocation(location);
        listing.setPrice(price);
        listing.setImageUrl("/uploads/" + fileName);
        return repository.save(listing);
    }

    @PutMapping("/{id}")
    public HomestayListing updateListing(@PathVariable Long id,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) String location,
                                         @RequestParam(required = false) Double price,
                                         @RequestParam(required = false) MultipartFile image) throws IOException {

        Optional<HomestayListing> existingListingOpt = repository.findById(id);
        if (!existingListingOpt.isPresent()) {
            throw new RuntimeException("Listing not found with id: " + id);
        }

        HomestayListing existingListing = existingListingOpt.get();

        // Update fields if new data is provided
        if (title != null && !title.isEmpty()) {
            existingListing.setTitle(title);
        }
        if (location != null && !location.isEmpty()) {
            existingListing.setLocation(location);
        }
        if (price != null) {
            existingListing.setPrice(price);
        }

        // Handle image upload if a new image is provided
        if (image != null && !image.isEmpty()) {
            // Ensure the upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Delete the old image if exists
            if (existingListing.getImageUrl() != null) {
                Path oldImagePath = Paths.get(uploadDir + existingListing.getImageUrl().replace("/uploads/", ""));
                Files.deleteIfExists(oldImagePath);
            }

            // Save the new image
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);

            // Update image URL
            existingListing.setImageUrl("/uploads/" + fileName);
        }

        // Save the updated listing
        return repository.save(existingListing);
    }


    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
