package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.parkme.adapters.CityPickerAdapter;
import com.example.parkme.adapters.LocationPickerAdapter;
import com.example.parkme.R;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.response.CitiesResponse;
import com.example.parkme.api.response.DestinationsResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.constants.EventBusConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.events.BaseEvents;
import com.example.parkme.models.StationModel;
import com.example.parkme.utils.AppHelper;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.PickMeEventBus;
import com.example.parkme.utils.RetrofitErrorHelper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.activities.HomeActivity;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickDestinationFragment extends Fragment {

    @BindView(R.id.spinner_pick_location)
    Spinner mSpinnerPickLocation;
    @BindView(R.id.radio_vehicle_type)
    RadioGroup mRadioGroupVehicleType;
    @BindView(R.id.spinner_pick_city)
    Spinner mSpinnerCity;
    @BindView(R.id.layout_root)
    ScrollView mRootView;
    @BindView(R.id.tv_destination)
    TextView mTextViewDestination;
    @BindView(R.id.tv_vehicle_type)
    TextView mTextViewVehicleType;
    @BindView(R.id.view_details)
    Button mBtnViewDetails;

    private ProgressDialog mProgressDialog;
    private List<StationModel> mStationModelList = new ArrayList<>();
    private List<String> mCityList = new ArrayList<>();
    private LocationPickerAdapter mDestinationAdapter;
    private int mSelectedDestinationID = AppConstants.no_selection;


    public PickDestinationFragment() {
        // Required empty public constructor
    }


    /**
     * Returns an instance of PickDestinationFragment
     * @return
     */
    public static PickDestinationFragment newInstance() {
        PickDestinationFragment fragment = new PickDestinationFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_destination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mProgressDialog = new ProgressDialog(getContext());
        callCitiesApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.find_parking));
        PickMeEventBus.getInstance().register(this);
        mCityList.clear();
    }

    @Override
    public void onStop() {
        PickMeEventBus.getInstance().unregister(this);
        super.onStop();
    }

    private void callCitiesApi() {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            User user = UserTableHelper.getCurrentUser();
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<CitiesResponse> call = apiInterface.getCities(user.getSessionKey());
            call.enqueue(new Callback<CitiesResponse>() {
                @Override
                public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response != null && response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            PickMeEventBus.getInstance().post(
                                    new BaseEvents(EventBusConstants.EVENT_CITY, response.body()));
                        } else {
                            Helper.showSnackBar(response.body().getMessage(), mRootView);
                        }
                    else {
                        Gson gson = new Gson();
                        RetrofitErrorHelper message= gson.fromJson(
                                response.errorBody().charStream(),
                                (Type) RetrofitErrorHelper.class);
                        Helper.showSnackBar(message.getMessage(), mRootView);
                    }
                }

                @Override
                public void onFailure(Call<CitiesResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }


    private void displayCityList(List<String> cityResponse) {
        if(cityResponse!=null && cityResponse.size()>0){
            mCityList.addAll(cityResponse);
            setCitySpinnerAdapter();
        } else {
            Helper.showSnackBar(getString(R.string.no_cities_available), mRootView);
        }
    }

    private void setCitySpinnerAdapter() {
        mSpinnerCity.setAdapter(new CityPickerAdapter(mCityList));
        Helper.dismissProgress(mProgressDialog);
        mSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callDestinationsAPI(mCityList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void callDestinationsAPI(String selectedCity) {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            User user = UserTableHelper.getCurrentUser();
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<DestinationsResponse> call = apiInterface.getDestinations(user.getSessionKey(), selectedCity);
            call.enqueue(new Callback<DestinationsResponse>() {
                @Override
                public void onResponse(Call<DestinationsResponse> call, Response<DestinationsResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            PickMeEventBus.getInstance().post(
                                    new BaseEvents(EventBusConstants.EVENT_DESTINATION, response.body()));
                        } else {
                            Helper.showSnackBar(response.body().getMessage(), mRootView);
                        }
                    else {
                        Gson gson = new Gson();
                        RetrofitErrorHelper message= gson.fromJson(
                                response.errorBody().charStream(),
                                (Type) RetrofitErrorHelper.class);
                        Helper.showSnackBar(message.getMessage(), mRootView);
                    }
                }

                @Override
                public void onFailure(Call<DestinationsResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }

    private void displayDestinationsInTheCity(List<StationModel> destinationsResponse) {
        mTextViewDestination.setVisibility(View.VISIBLE);
        mSpinnerPickLocation.setVisibility(View.VISIBLE);
        if(destinationsResponse!=null && destinationsResponse.size()>0){
            if(mStationModelList.size()==0){
                mStationModelList.addAll(destinationsResponse);
            } else {
                mStationModelList.clear();
                mStationModelList.addAll(destinationsResponse);
                mDestinationAdapter.notifyDataSetChanged();
            }
            setDestinationsSpinnerAdapter();
        } else {
            Helper.showSnackBar(getString(R.string.no_parking_available), mRootView);
        }
    }

    private void setDestinationsSpinnerAdapter() {
        mDestinationAdapter = new LocationPickerAdapter(mStationModelList);
        mSpinnerPickLocation.setAdapter(mDestinationAdapter);
        Helper.dismissProgress(mProgressDialog);
        mSpinnerPickLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedDestinationID = mStationModelList.get(position).getId();
                displaySelectVehicleType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedDestinationID = mStationModelList.get(0).getId();
            }
        });
    }

    private void displaySelectVehicleType(){
        mTextViewVehicleType.setVisibility(View.VISIBLE);
        mRadioGroupVehicleType.setVisibility(View.VISIBLE);
        mBtnViewDetails.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.view_details)
    void onClickViewParkingDetails(){
        int selectedVehicleType;
        int selectedRadioBtnID = mRadioGroupVehicleType.getCheckedRadioButtonId();
        RadioButton  radioButton= (RadioButton) getActivity().findViewById(selectedRadioBtnID);
        if(radioButton.getText().equals(AppConstants.FOUR_WHEELER)){
            selectedVehicleType = AppConstants.four_wheeler;
        } else {
            selectedVehicleType = AppConstants.two_wheeler;
        }
        AppHelper.addFragment(getContext(), ParkingLocationDetailsFragment
                .newInstance(mSelectedDestinationID, selectedVehicleType),true);
    }

    /**
     * Event to receive date selected by the user
     * @param events
     */
    public void onEvent(BaseEvents events){
        if(events.getmEventType() == EventBusConstants.EVENT_CITY){
            CitiesResponse response = (CitiesResponse)events.getmObject();
            displayCityList(response.getCityList());
        }else if(events.getmEventType() == EventBusConstants.EVENT_DESTINATION){
            DestinationsResponse response = (DestinationsResponse) events.getmObject();
            displayDestinationsInTheCity(response.getDestinationList());
        }
    }
}
