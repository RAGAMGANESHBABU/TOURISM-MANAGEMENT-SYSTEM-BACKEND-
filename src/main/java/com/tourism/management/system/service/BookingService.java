package com.tourism.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourism.management.system.model.Booking;
import com.tourism.management.system.model.HomestayListing;
import com.tourism.management.system.repository.BookingRepository;
import com.tourism.management.system.repository.HomestayRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HomestayRepository homestayRepository;

    // Create a new booking
    public Booking createBooking(Booking booking) {
        if (booking.getHomestayId() == null) {
            throw new IllegalArgumentException("Homestay ID cannot be null");
        }

        // Ensure that homestayId is being set correctly
        System.out.println("Booking Details: " + booking.getCustomerName() + ", Homestay ID: " + booking.getHomestayId());

        // Save the booking to the database
        return bookingRepository.save(booking);
    }

    // Update booking status
    @Transactional
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            Booking existingBooking = booking.get();
            existingBooking.setStatus(status); // Update the status of the booking
            return bookingRepository.save(existingBooking); // Save the updated booking
        }
        throw new IllegalArgumentException("Booking not found with ID: " + bookingId);
    }

    // Get all bookings with associated homestay details
    @Transactional
    public List<Booking> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        // For each booking, fetch the Homestay details based on the homestayId
        for (Booking booking : bookings) {
            HomestayListing homestay = homestayRepository.findById(booking.getHomestayId()).orElse(null);
            booking.setHomestay(homestay); // Set the fetched homestay into the booking object
        }

        return bookings;
    }

    // Delete a booking by ID
    public boolean deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
