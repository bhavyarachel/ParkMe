package com.example.parkme.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleModel  implements Serializable {

    private int id;
    @SerializedName("company")
    private String vehicleCompany;
    @SerializedName("model")
    private String vehicleModel;
    @SerializedName("vehicle_number")
    private String vehicleNumber;
    @SerializedName("vehicle_type")
    private int vehicleType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleCompany() {
        return vehicleCompany;
    }

    public void setVehicleCompany(String vehicleCompany) {
        this.vehicleCompany = vehicleCompany;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }
}
