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
import com.example.parkme.api.request.EditVehicleRequestBean;
import com.example.parkme.api.response.EditVehicleResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.models.VehicleModel;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.RetrofitErrorHelper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.activities.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditVehicleFragment extends Fragment {

    @BindView(R.id.et_vehicle_reg_number)
    TextInputEditText mEditTextVehicleRegistrationNumber;
    @BindView(R.id.et_vehicle_company)
    TextInputEditText mEditTextVehicleCompany;
    @BindView(R.id.et_vehicle_model)
    TextInputEditText mEditTextVehicleModel;
    @BindView(R.id.radio_vehicle_type)
    RadioGroup mRadioGroupVehicleType;
    @BindView(R.id.radio_four_wheeler)
    RadioButton mRadioButtonFourWheeler;
    @BindView(R.id.radio_two_wheeler)
    RadioButton mRadioButtonTwoWheeler;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;

    private VehicleModel mVehicleModel;
    private ProgressDialog mProgressDialog;

    public EditVehicleFragment() {
        // Required empty public constructor
    }


    public static EditVehicleFragment newInstance(VehicleModel vehicleModel) {
        EditVehicleFragment fragment = new EditVehicleFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.BUNDLE_SELECTED_VEHICLE, (Serializable) vehicleModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_vehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.edit_vehicle_details));
        mProgressDialog = new ProgressDialog(getContext());
        mVehicleModel = (VehicleModel) getArguments().getSerializable(AppConstants.BUNDLE_SELECTED_VEHICLE);
        setVehicleDetails();
    }

    private void setVehicleDetails() {
        mEditTextVehicleRegistrationNumber.setText(mVehicleModel.getVehicleNumber());
        mEditTextVehicleCompany.setText(mVehicleModel.getVehicleCompany());
        mEditTextVehicleModel.setText(mVehicleModel.getVehicleModel());
        int vehicleType = mVehicleModel.getVehicleType();
        if(vehicleType == AppConstants.four_wheeler){
            mRadioButtonFourWheeler.setChecked(true);
        } else {
            mRadioButtonTwoWheeler.setChecked(true);
        }
    }

    @OnClick(R.id.btn_submit)
    void onClickSubmit(){
        boolean isValid = validateFields();
        if(isValid){
            EditVehicleRequestBean requestBean = new EditVehicleRequestBean();
            requestBean.setVehicleNumber(mEditTextVehicleRegistrationNumber.getText().toString());
            requestBean.setVehicleCompany(mEditTextVehicleCompany.getText().toString());
            requestBean.setVehicleModel(mEditTextVehicleModel.getText().toString());
            int selectedVehicleType;
            int selectedRadioBtnID = mRadioGroupVehicleType.getCheckedRadioButtonId();
            RadioButton radioButton= (RadioButton) getActivity().findViewById(selectedRadioBtnID);
            if(radioButton.getText().equals(AppConstants.FOUR_WHEELER)){
                selectedVehicleType = AppConstants.four_wheeler;
            } else {
                selectedVehicleType = AppConstants.two_wheeler;
            }
            requestBean.setVehicleType(selectedVehicleType);
            callEditVehicleAPI(requestBean);
        } else {
            Helper.showSnackBar(getString(R.string.all_fields_are_mandatory),mRootView);
        }
    }

    private boolean validateFields() {
        if(!TextUtils.isEmpty(mEditTextVehicleRegistrationNumber.getText()) &&
            !TextUtils.isEmpty(mEditTextVehicleModel.getText()) &&
            !TextUtils.isEmpty(mEditTextVehicleCompany.getText())){
            return true;
        }
        return false;
    }

    private void callEditVehicleAPI(EditVehicleRequestBean requestBean) {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        User user = UserTableHelper.getCurrentUser();
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<EditVehicleResponse> call = apiInterface.editVehicle(user.getSessionKey(),mVehicleModel.getId(),requestBean);
            call.enqueue(new Callback<EditVehicleResponse>() {
                @Override
                public void onResponse(Call<EditVehicleResponse> call, Response<EditVehicleResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            Helper.showSnackBar(response.body().getMessage(), mRootView);
                            getActivity().onBackPressed();
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
                public void onFailure(Call<EditVehicleResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }
}
