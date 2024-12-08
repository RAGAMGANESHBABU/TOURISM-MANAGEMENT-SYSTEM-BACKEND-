package com.tourism.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.management.system.model.HomestayListing;

public interface HomestayRepository extends JpaRepository<HomestayListing, Long> {
}
