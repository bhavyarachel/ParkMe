package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.adapters.VehicleListAdapter;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.response.VehiclesResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.utils.AppHelper;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.RetrofitErrorHelper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.activities.HomeActivity;
import com.google.gson.Gson;

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
public class UserProfileFragment extends Fragment {

    @BindView(R.id.layout_root)
    ScrollView mRootView;
    @BindView(R.id.rv_user_vehicles)
    RecyclerView mRecyclerViewVehicles;
    @BindView(R.id.tv_user_name)
    TextView mTextViewUserName;
    @BindView(R.id.tv_no_vehicles_to_show)
    TextView mTextViewNoVehicles;

    private ProgressDialog mProgressDialog;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
        User user = UserTableHelper.getCurrentUser();
        mTextViewUserName.setText(user.getUserName());
        callVehiclesAPI(user.getSessionKey());
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.user_profile));

    }


    private void callVehiclesAPI(String sessionKey) {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<VehiclesResponse> call = apiInterface.getAllVehicles(sessionKey);
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
        if(vehiclesResponse.getVehicleModelList().size()==0)
        {
            mTextViewNoVehicles.setVisibility(View.VISIBLE);
        }
        VehicleListAdapter adapter = new VehicleListAdapter(vehiclesResponse.getVehicleModelList());
        mRecyclerViewVehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewVehicles.setAdapter(adapter);
    }

    @OnClick(R.id.btn_add_vehicle)
    void onClickAddVehicle(){
        AppHelper.addFragment(getContext(), AddNewVehicleFragment.newInstance(), true);
    }
}
