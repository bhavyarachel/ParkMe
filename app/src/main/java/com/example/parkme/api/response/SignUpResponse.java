package com.example.parkme.api.response;

import com.example.parkme.models.UserModel;

public class SignUpResponse extends BaseResponse {

    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
