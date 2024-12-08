package com.tourism.management.system.repository;

import com.tourism.management.system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods if needed
}
