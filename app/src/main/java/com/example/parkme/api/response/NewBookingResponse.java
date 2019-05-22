package com.example.parkme.api.response;

import com.example.parkme.models.BookingModel;
import com.google.gson.annotations.SerializedName;

public class NewBookingResponse extends BaseResponse{

    @SerializedName("booking")
    private BookingModel bookingModel;

    @SerializedName("success")
    private boolean success;

    public BookingModel getBookingModel() {
        return bookingModel;
    }

    public void setBookingModel(BookingModel bookingModel) {
        this.bookingModel = bookingModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}


