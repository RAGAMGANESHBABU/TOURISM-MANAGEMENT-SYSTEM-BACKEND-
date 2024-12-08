package com.tourism.management.system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.tourism.management.system.model.Recommendation;
import com.tourism.management.system.repository.RecommendationRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final RecommendationRepository repository;

    public RecommendationController(RecommendationRepository repository) {
        this.repository = repository;
    }

    // Get all recommendations
    @GetMapping
    public List<Recommendation> getAllRecommendations() {
        return repository.findAll();
    }

    // Add a new recommendation with image
    @PostMapping
    public Recommendation addRecommendation(@RequestParam("name") String name,
                                            @RequestParam("description") String description,
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

        // Save recommendation details
        Recommendation recommendation = new Recommendation();
        recommendation.setName(name);
        recommendation.setDescription(description);
        recommendation.setImageUrl("/uploads/" + fileName);
        return repository.save(recommendation);
    }

    // Update an existing recommendation with optional fields
    @PutMapping("/{id}")
    public Recommendation updateRecommendation(@PathVariable Long id,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String description,
                                               @RequestParam(required = false) MultipartFile image) throws IOException {

        Optional<Recommendation> existingRecommendationOpt = repository.findById(id);
        if (!existingRecommendationOpt.isPresent()) {
            throw new RuntimeException("Recommendation not found with id: " + id);
        }

        Recommendation existingRecommendation = existingRecommendationOpt.get();

        // Update fields if new data is provided
        if (name != null && !name.isEmpty()) {
            existingRecommendation.setName(name);
        }
        if (description != null && !description.isEmpty()) {
            existingRecommendation.setDescription(description);
        }

        // Handle image upload if a new image is provided
        if (image != null && !image.isEmpty()) {
            // Ensure the upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Delete the old image if exists
            if (existingRecommendation.getImageUrl() != null) {
                Path oldImagePath = Paths.get(uploadDir + existingRecommendation.getImageUrl().replace("/uploads/", ""));
                Files.deleteIfExists(oldImagePath);
            }

            // Save the new image
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);

            // Update image URL
            existingRecommendation.setImageUrl("/uploads/" + fileName);
        }

        // Save the updated recommendation
        return repository.save(existingRecommendation);
    }

    // Delete a recommendation and its image
    @DeleteMapping("/{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        Optional<Recommendation> existingRecommendationOpt = repository.findById(id);

        if (existingRecommendationOpt.isPresent() && existingRecommendationOpt.get().getImageUrl() != null) {
            Path oldImagePath = Paths.get(uploadDir + existingRecommendationOpt.get().getImageUrl().replace("/uploads/", ""));
            try {
                Files.deleteIfExists(oldImagePath);
            } catch (IOException e) {
                e.printStackTrace(); // Log the error
            }
        }

        repository.deleteById(id);
    }
}
