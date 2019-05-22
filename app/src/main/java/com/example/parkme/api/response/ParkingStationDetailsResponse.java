package com.example.parkme.api.response;

import com.example.parkme.models.StationDetailsModel;
import com.google.gson.annotations.SerializedName;

public class ParkingStationDetailsResponse extends BaseResponse {

    @SerializedName("station")
    private StationDetailsModel stationDetailsModel;

    @SerializedName("success")
    private boolean success;

    public StationDetailsModel getStationDetailsModel() {
        return stationDetailsModel;
    }

    public void setStationDetailsModel(StationDetailsModel stationDetailsModel) {
        this.stationDetailsModel = stationDetailsModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}



