package com.example.parkme.api.response;

import com.example.parkme.models.UserModel;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse{

    @SerializedName("user")
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
