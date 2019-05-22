package com.example.parkme.models;

import com.google.gson.annotations.SerializedName;

public class StationModel {

    private int id;
    @SerializedName("name")
    private String destinationName;

    public StationModel(int id, String destinationName, int latitude, int longitude) {
        this.id = id;
        this.destinationName = destinationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
