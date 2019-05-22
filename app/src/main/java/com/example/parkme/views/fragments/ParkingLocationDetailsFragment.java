package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.media.Rating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.response.ParkingStationDetailsResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.utils.AppHelper;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.RetrofitErrorHelper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.activities.HomeActivity;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
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
public class ParkingLocationDetailsFragment extends Fragment {

    @BindView(R.id.destination_name)
    TextView mTextViewDestinationName;
    @BindView(R.id.vehicle_type)
    TextView mTextViewVehicleType;
    @BindView(R.id.free_slots)
    TextView mTextViewFreeSlots;
    @BindView(R.id.btn_book_slot)
    Button mBtnBookSlot;
    @BindView(R.id.layout_root)
    ScrollView mRootView;
    @BindView(R.id.rating_parking)
    RatingBar mRatingBar;
    @BindView(R.id.tv_address)
    TextView mTextViewAddress;
    @BindView(R.id.tv_total_slots)
    TextView mTextViewTotalSlots;

    private int mSelectedDestinationID;
    private int mSelectedVehicleType;
    private ProgressDialog mProgressDialog;
    private String mDestinationName;
    private int mDestinationID;

    public ParkingLocationDetailsFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param selectedDestinationID
     * @param selectedVehicleType
     * @return
     */
    public static ParkingLocationDetailsFragment newInstance(int selectedDestinationID, int selectedVehicleType) {
        ParkingLocationDetailsFragment fragment = new ParkingLocationDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_SELECTED_DESTINATION_ID, selectedDestinationID);
        args.putInt(AppConstants.BUNDLE_SELECTED_VEHICLE_TYPE, selectedVehicleType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parking_location_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.parking_location_details));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mProgressDialog = new ProgressDialog(getContext());
        mSelectedDestinationID = getArguments().getInt(AppConstants.BUNDLE_SELECTED_DESTINATION_ID);
        mSelectedVehicleType = getArguments().getInt(AppConstants.BUNDLE_SELECTED_VEHICLE_TYPE);
        callParkingDetailsAPI();
    }

    private void callParkingDetailsAPI() {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            User user = UserTableHelper.getCurrentUser();
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<ParkingStationDetailsResponse> call = apiInterface.getParkingLocationDetails(
                    user.getSessionKey(), mSelectedDestinationID, mSelectedVehicleType);
            call.enqueue(new Callback<ParkingStationDetailsResponse>() {
                @Override
                public void onResponse(Call<ParkingStationDetailsResponse> call, Response<ParkingStationDetailsResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response != null && response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            handleDetailsResponse(response.body());
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
                public void onFailure(Call<ParkingStationDetailsResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }

    private void handleDetailsResponse(ParkingStationDetailsResponse detailsResponse) {
        if(detailsResponse.getStationDetailsModel() != null){
            mDestinationName = detailsResponse.getStationDetailsModel().getName();
            mDestinationID = detailsResponse.getStationDetailsModel().getId();
            mTextViewDestinationName.setText(mDestinationName);
            if(mSelectedVehicleType == 0){
                mTextViewVehicleType.setText(AppConstants.FOUR_WHEELER);
            } else {
                mTextViewVehicleType.setText(AppConstants.TWO_WHEELER);
            }
            int freeSlots = detailsResponse.getStationDetailsModel().getAvailableSlots();
            mTextViewFreeSlots.setText(String.valueOf(freeSlots));
            if(freeSlots==0){
                mBtnBookSlot.setEnabled(false);
            }
            mRatingBar.setRating(detailsResponse.getStationDetailsModel().getReview());
            mTextViewAddress.setText(detailsResponse.getStationDetailsModel().getAddress());
            mTextViewTotalSlots.setText(String.valueOf(detailsResponse.getStationDetailsModel().getTotalSlots()));
        }
    }

    @OnClick(R.id.btn_book_slot)
    void onClickBookSlot(){
        if(mBtnBookSlot.isEnabled()){
            AppHelper.addFragment(getContext(), ConfirmBookingFragment.newInstance(
                    mDestinationName, mSelectedVehicleType, mDestinationID),true);
        } else {
            Helper.showSnackBar(getString(R.string.no_slot_available_for_parking), mRootView);
        }
    }
}
