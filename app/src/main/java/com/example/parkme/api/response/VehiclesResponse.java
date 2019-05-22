package com.example.parkme.api.response;

import com.example.parkme.models.VehicleModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclesResponse extends BaseResponse{

    @SerializedName("success")
    private boolean success;

    @SerializedName("vehicles")
    private List<VehicleModel> vehicleModelList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<VehicleModel> getVehicleModelList() {
        return vehicleModelList;
    }

    public void setVehicleModelList(List<VehicleModel> vehicleModelList) {
        this.vehicleModelList = vehicleModelList;
    }
}