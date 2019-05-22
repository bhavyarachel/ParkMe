package com.example.parkme.api.response;

import com.example.parkme.models.StationModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DestinationsResponse extends BaseResponse{

    private boolean success;

    @SerializedName("stations")
    private List<StationModel> destinationList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<StationModel> getDestinationList() {
        return destinationList;
    }

    public void setDestinationList(List<StationModel> destinationList) {
        this.destinationList = destinationList;
    }
}


