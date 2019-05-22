package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.adapters.VehicleRegNumberPickerAdapter;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.request.NewBookingRequestBean;
import com.example.parkme.api.response.NewBookingResponse;
import com.example.parkme.api.response.VehiclesResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.models.VehicleModel;
import com.example.parkme.utils.Helper;
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
public class ConfirmBookingFragment extends Fragment {

    @BindView(R.id.tv_no_vehicles)
    TextView mTextViewNoVehiclesAdded;
    @BindView(R.id.destination_name)
    TextView mTextViewDestinationName;
    @BindView(R.id.vehicle_type)
    TextView mTextViewVehicleType;
    @BindView(R.id.spinner_pick_vehicle_reg_number)
    Spinner mSpinnerVehicleRegNumber;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;

    private String mDestinationName;
    private int mVehicleType;
    private ProgressDialog mProgressDialog;
    private int mSelectedVehicleID;
    private int mDestinationID;
    private List<VehicleModel> mFilteredVehicleList = new ArrayList<>();


    public ConfirmBookingFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param destinationName
     * @param selectedVehicleType
     * @param selectedDestinationID
     * @return
     */
    public static ConfirmBookingFragment newInstance(String destinationName, int selectedVehicleType, int selectedDestinationID) {
        ConfirmBookingFragment fragment = new ConfirmBookingFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.BUNDLE_SELECTED_DESTINATION_NAME, destinationName);
        args.putInt(AppConstants.BUNDLE_SELECTED_VEHICLE_TYPE, selectedVehicleType);
        args.putInt(AppConstants.BUNDLE_SELECTED_DESTINATION_ID, selectedDestinationID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mProgressDialog = new ProgressDialog(getContext());
        mDestinationName = getArguments().getString(AppConstants.BUNDLE_SELECTED_DESTINATION_NAME);
        mVehicleType = getArguments().getInt(AppConstants.BUNDLE_SELECTED_VEHICLE_TYPE);
        mDestinationID = getArguments().getInt(AppConstants.BUNDLE_SELECTED_DESTINATION_ID);
        displayBookingDetails();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.confirm_booking));
    }

    private void displayBookingDetails() {
        mTextViewDestinationName.setText(mDestinationName);
        if(mVehicleType == AppConstants.four_wheeler){
            mTextViewVehicleType.setText(AppConstants.FOUR_WHEELER);
        } else {
            mTextViewVehicleType.setText(AppConstants.TWO_WHEELER);
        }
        callVehicleRegNumbersAPI();
    }

    private void callVehicleRegNumbersAPI() {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        User user = UserTableHelper.getCurrentUser();
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<VehiclesResponse> call = apiInterface.getAllVehicles(user.getSessionKey());
            call.enqueue(new Callback<VehiclesResponse>() {
                @Override
                public void onResponse(Call<VehiclesResponse> call, Response<VehiclesResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            handleVehiclesResponse(response.body());
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
                public void onFailure(Call<VehiclesResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }

    private void handleVehiclesResponse(VehiclesResponse vehiclesResponse) {
        for (VehicleModel vehicle: vehiclesResponse.getVehicleModelList()) {
            if(vehicle.getVehicleType()==mVehicleType){
                mFilteredVehicleList.add(vehicle);
            }
        }
        if(mFilteredVehicleList.size()==0)
        {
            mTextViewNoVehiclesAdded.setVisibility(View.VISIBLE);
        }
        mSpinnerVehicleRegNumber.setAdapter(new VehicleRegNumberPickerAdapter(mFilteredVehicleList));
        Helper.dismissProgress(mProgressDialog);
        mSpinnerVehicleRegNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedVehicleID = mFilteredVehicleList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedVehicleID = mFilteredVehicleList.get(0).getId();
            }
        });
    }

    @OnClick(R.id.btn_confirm_booking)
    void onClickConfirmBooking(){
        NewBookingRequestBean bookingRequestBean = new NewBookingRequestBean();
        bookingRequestBean.setVehicleType(mVehicleType);
        bookingRequestBean.setVehicleId(mSelectedVehicleID);
        bookingRequestBean.setStationId(mDestinationID);
        callConfirmBookingAPI(bookingRequestBean);
    }

    private void callConfirmBookingAPI(NewBookingRequestBean bookingRequestBean) {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        User user = UserTableHelper.getCurrentUser();
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<NewBookingResponse> call = apiInterface.createNewBooking(user.getSessionKey(), bookingRequestBean);
            call.enqueue(new Callback<NewBookingResponse>() {
                @Override
                public void onResponse(Call<NewBookingResponse> call, Response<NewBookingResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            handleBookingResponse(response.body());
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
                public void onFailure(Call<NewBookingResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }

    private void handleBookingResponse(NewBookingResponse newBookingResponse) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(newBookingResponse.getMessage());
        alertDialogBuilder
                .setMessage("Your booking Number is : "+ newBookingResponse.getBookingModel()
                        .getBookingNumber())
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        getActivity().finish();
                        startHomeActivity();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void startHomeActivity() {
        Intent activityIntent = new Intent();
        activityIntent.setClass(getContext(), HomeActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activityIntent);
    }
}
