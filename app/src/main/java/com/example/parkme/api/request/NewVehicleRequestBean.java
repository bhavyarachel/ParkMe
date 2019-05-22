package com.example.parkme.api.request;

import com.google.gson.annotations.SerializedName;

public class NewVehicleRequestBean {

    @SerializedName("vehicle_number")
    private String vehicleNumber;
    @SerializedName("company")
    private String vehicleCompany;
    @SerializedName("model")
    private String vehicleModel;
    @SerializedName("vehicle_type")
    private int vehicleType;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
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

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }
}
