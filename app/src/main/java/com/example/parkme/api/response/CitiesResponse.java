package com.example.parkme.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesResponse extends BaseResponse {

    private boolean success;

    @SerializedName("cities")
    private List<String> cityList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }
}
