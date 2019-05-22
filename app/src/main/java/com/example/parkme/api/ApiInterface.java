package com.example.parkme.api;

import com.example.parkme.api.request.EditVehicleRequestBean;
import com.example.parkme.api.request.LoginRequestBean;
import com.example.parkme.api.request.NewBookingRequestBean;
import com.example.parkme.api.request.NewVehicleRequestBean;
import com.example.parkme.api.request.SignUpRequestBean;
import com.example.parkme.api.response.BaseResponse;
import com.example.parkme.api.response.BookingsResponse;
import com.example.parkme.api.response.CitiesResponse;
import com.example.parkme.api.response.DestinationsResponse;
import com.example.parkme.api.response.EditVehicleResponse;
import com.example.parkme.api.response.LoginResponse;
import com.example.parkme.api.response.NewBookingResponse;
import com.example.parkme.api.response.NewVehicleResponse;
import com.example.parkme.api.response.ParkingStationDetailsResponse;
import com.example.parkme.api.response.VehiclesResponse;
import com.example.parkme.constants.ApiConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @POST(ApiConstants.LOGIN_API)
    Call<LoginResponse> login(@Body LoginRequestBean loginRequest);

    @POST(ApiConstants.SIGN_UP_API)
    Call<LoginResponse> signUp(@Body SignUpRequestBean signUpRequest);

    @GET(ApiConstants.CITIES_API)
    Call<CitiesResponse> getCities(@Header (ApiConstants.SESSION_KEY_HEADER) String sessionKey);

    @GET(ApiConstants.DESTINATIONS_API)
    Call<DestinationsResponse> getDestinations(
            @Header (ApiConstants.SESSION_KEY_HEADER) String sessionKey,
            @Query(ApiConstants.CITY_NAME) String cityName);

    @GET(ApiConstants.PARKING_DETAILS_API)
    Call<ParkingStationDetailsResponse> getParkingLocationDetails(
            @Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey,
            @Path(ApiConstants.DESTINATION_ID) int destinationID,
            @Query(ApiConstants.PARKING_TYPE) int parkingType);

    @POST(ApiConstants.BOOKINGS_API)
    Call<NewBookingResponse> createNewBooking(
            @Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey,
            @Body NewBookingRequestBean newRequestModel);

    @GET(ApiConstants.BOOKINGS_API)
    Call<BookingsResponse> getAllBookings(
            @Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey);

    @POST(ApiConstants.VEHICLES_API)
    Call<NewVehicleResponse> createNewVehicle(
            @Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey,
            @Body NewVehicleRequestBean newVehicleRequestModel);

    @GET(ApiConstants.VEHICLES_API)
    Call<VehiclesResponse> getAllVehicles(
            @Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey);

    @PATCH(ApiConstants.EDIT_VEHICLE_API)
    Call<EditVehicleResponse> editVehicle(
            @Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey,
            @Path(ApiConstants.VEHICLE_ID) int vehicleID,
            @Body EditVehicleRequestBean editVehicleRequest);

    @DELETE(ApiConstants.LOGOUT)
    Call<BaseResponse> logout(@Header(ApiConstants.SESSION_KEY_HEADER) String sessionKey);

}
