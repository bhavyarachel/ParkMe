package com.example.parkme.models;

import com.google.gson.annotations.SerializedName;

public class StationDetailsModel {

    private int id;
    private String name;
    private String address;
    private float fare;
    @SerializedName("total_slots")
    private int totalSlots;
    @SerializedName("available_slots")
    private int availableSlots;
    private float review;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public float getReview() {
        return review;
    }

    public void setReview(float review) {
        this.review = review;
    }
}
