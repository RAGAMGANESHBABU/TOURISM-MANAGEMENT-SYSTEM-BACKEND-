package com.tourism.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.management.system.model.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}

