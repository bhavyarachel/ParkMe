package com.example.parkme.models;

import com.google.gson.annotations.SerializedName;

public class BookingModel {

    private int id;
    @SerializedName("booking_number")
    private String bookingNumber;
    @SerializedName("booking_status")
    private String bookingStatus;
    @SerializedName("booking_expiry")
    private String bookingExpiry;
    @SerializedName("vehicle_number")
    private String vehicleNumber;
    @SerializedName("station")
    private String destinationName;
    @SerializedName("address")
    private String address;
    @SerializedName("booked_at")
    private String bookedAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingExpiry() {
        return bookingExpiry;
    }

    public void setBookingExpiry(String bookingExpiry) {
        this.bookingExpiry = bookingExpiry;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(String bookedAt) {
        this.bookedAt = bookedAt;
    }
}
