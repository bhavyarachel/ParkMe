package com.example.parkme.api.response;

import com.example.parkme.models.VehicleModel;
import com.google.gson.annotations.SerializedName;

public class NewVehicleResponse extends BaseResponse{

    @SerializedName("vehicle")
    private VehicleModel vehicleModel;

    @SerializedName("success")
    private boolean success;

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}


