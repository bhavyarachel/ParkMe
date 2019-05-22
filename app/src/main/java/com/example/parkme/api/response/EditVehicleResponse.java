package com.example.parkme.api.response;

import com.example.parkme.models.VehicleModel;
import com.google.gson.annotations.SerializedName;

public class EditVehicleResponse extends BaseResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("vehicle")
    private VehicleModel vehicleModel;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
}
