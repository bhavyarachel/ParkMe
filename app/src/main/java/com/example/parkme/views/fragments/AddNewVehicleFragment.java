package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.parkme.R;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.request.NewVehicleRequestBean;
import com.example.parkme.api.response.NewVehicleResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.RetrofitErrorHelper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.activities.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
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
public class AddNewVehicleFragment extends Fragment {

    @BindView(R.id.radio_vehicle_type)
    RadioGroup mRadioGroupVehicleType;
    @BindView(R.id.et_vehicle_reg_number)
    TextInputEditText mEditTextVehicleRegistrationNumber;
    @BindView(R.id.et_vehicle_company)
    TextInputEditText mEditTextVehicleCompany;
    @BindView(R.id.et_vehicle_model)
    TextInputEditText mEditTextVehicleModel;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;

    private ProgressDialog mProgressDialog;


    public AddNewVehicleFragment() {
        // Required empty public constructor
    }

    public static AddNewVehicleFragment newInstance() {
        AddNewVehicleFragment fragment = new AddNewVehicleFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_vehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.add_vehicle));
    }

    @OnClick(R.id.btn_add_vehicle)
    void onClickAddNewVehicle(){
        boolean isValid = validateVehicleDetails();
        NewVehicleRequestBean newVehicleRequestBean = new NewVehicleRequestBean();
        newVehicleRequestBean.setVehicleCompany(mEditTextVehicleCompany.getText().toString());
        newVehicleRequestBean.setVehicleModel(mEditTextVehicleModel.getText().toString());
        newVehicleRequestBean.setVehicleNumber(mEditTextVehicleRegistrationNumber.getText().toString());
        int selectedVehicleType;
        int selectedRadioBtnID = mRadioGroupVehicleType.getCheckedRadioButtonId();
        RadioButton radioButton= (RadioButton) getActivity().findViewById(selectedRadioBtnID);
        if(radioButton.getText().equals(AppConstants.FOUR_WHEELER)){
            selectedVehicleType = AppConstants.four_wheeler;
        } else {
            selectedVehicleType = AppConstants.two_wheeler;
        }
        newVehicleRequestBean.setVehicleType(selectedVehicleType);
        if(isValid){
            callCreateNewVehicleAPI(newVehicleRequestBean);
        }
    }

    private void callCreateNewVehicleAPI(NewVehicleRequestBean newVehicleRequestBean) {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        User user = UserTableHelper.getCurrentUser();

        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<NewVehicleResponse> call = apiInterface.createNewVehicle(user.getSessionKey(), newVehicleRequestBean);
            call.enqueue(new Callback<NewVehicleResponse>() {
                @Override
                public void onResponse(Call<NewVehicleResponse> call, Response<NewVehicleResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            handleNewVehicleResponse(response.body());
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
                public void onFailure(Call<NewVehicleResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }

    private boolean validateVehicleDetails() {
        if(!TextUtils.isEmpty(mEditTextVehicleRegistrationNumber.getText()) &&
                !TextUtils.isEmpty(mEditTextVehicleCompany.getText()) &&
                !TextUtils.isEmpty(mEditTextVehicleModel.getText())){
            return true;
        } else {
            Helper.showSnackBar(getString(R.string.all_fields_are_mandatory), mRootView);
            return false;
        }
    }

    private void handleNewVehicleResponse(NewVehicleResponse response) {
        Helper.showSnackBar(response.getMessage(), mRootView);
        getActivity().onBackPressed();
    }
}
