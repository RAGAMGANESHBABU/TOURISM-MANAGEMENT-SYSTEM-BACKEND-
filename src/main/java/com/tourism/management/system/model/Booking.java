package com.tourism.management.system.model;

import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.WAITING;  // Set default status to WAITING

    private Long homestayId;

    // Transient field to hold Homestay details
    @Transient
    private HomestayListing homestay;


    // Getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Long getHomestayId() {
        return homestayId;
    }

    public void setHomestayId(Long homestayId) {
        this.homestayId = homestayId;
    }

    public HomestayListing getHomestay() {
        return homestay;
    }

    public void setHomestay(HomestayListing homestay) {
        this.homestay = homestay;
    }

    public enum BookingStatus {
        WAITING, PENDING, CONFIRMED, CANCELED
    }
}
