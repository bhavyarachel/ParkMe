package com.example.parkme.api.request;

import com.google.gson.annotations.SerializedName;

public class NewBookingRequestBean {

    @SerializedName("station_id")
    private int stationId;

    @SerializedName("parking_type")
    private int vehicleType;

    @SerializedName("vehicle_id")
    private int vehicleId;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
}
