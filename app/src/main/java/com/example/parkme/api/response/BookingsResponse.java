package com.example.parkme.api.response;

import com.example.parkme.models.BookingModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingsResponse extends BaseResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("bookings")
    private List<BookingModel> bookingModelList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<BookingModel> getBookingModelList() {
        return bookingModelList;
    }

    public void setBookingModelList(List<BookingModel> bookingModelList) {
        this.bookingModelList = bookingModelList;
    }
}
