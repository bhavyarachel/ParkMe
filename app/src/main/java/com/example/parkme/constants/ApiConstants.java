package com.example.parkme.constants;

public class ApiConstants {

    public static final String API_BASE_URL = "https://book-my-space.herokuapp.com";
    public static final String LOGIN_API = "/api/v1/sign_in";
    public static final String SIGN_UP_API = "/api/v1/sign_up";
    public static final String CITIES_API = "/api/v1/cities";
    public static final String DESTINATIONS_API = "/api/v1/stations";
    public static final String PARKING_DETAILS_API = "/api/v1/stations/{id}";
    public static final String BOOKINGS_API = "/api/v1/bookings";
    public static final String VEHICLES_API = "/api/v1/vehicles";
    public static final String EDIT_VEHICLE_API = "/api/v1/vehicles/{id}";
    public static final String LOGOUT = "/api/v1/sign_out";


    public static final String KEY_ERROR = "error";

    public static final String SESSION_KEY_HEADER = "session-key";
    public static final String CITY_NAME = "city";
    public static final String DESTINATION_ID = "id";
    public static final String PARKING_TYPE = "parking_type";
    public static final String VEHICLE_ID = "id";


    public static final int STATUS_SUCCESS = 200;

    public static final String BOOKING_STATUS_BOOKED = "booked";
    public static final String BOOKING_STATUS_LEFT ="left";
}
