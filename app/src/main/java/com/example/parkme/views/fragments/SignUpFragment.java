package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.parkme.R;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.request.SignUpRequestBean;
import com.example.parkme.api.response.LoginResponse;
import com.example.parkme.constants.ApiConstants;
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
public class SignUpFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    @BindView(R.id.et_email_sign_up)
    TextInputEditText mEditTextEmail;
    @BindView(R.id.et_username_sign_up)
    TextInputEditText mEditTextUserName;
    @BindView(R.id.et_phone_sign_up)
    TextInputEditText mEditTextPhoneNumber;
    @BindView(R.id.et_password_sign_up)
    TextInputEditText mEditTextPassword;
    @BindView(R.id.et_confirm_password_sign_up)
    TextInputEditText mEditTextConfirmPassword;
    @BindView(R.id.layout_sign_up)
    LinearLayout mRootView;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Returns an instance of LoginFragment
     * @return
     */
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mProgressDialog = new ProgressDialog(getContext());
    }

    @OnClick(R.id.btn_sign_up)
    void onClickSignUp(){
        if (validateAllFields()) {
            mProgressDialog.setMessage(getString(R.string.loading));
            Helper.showProgress(mProgressDialog);
            SignUpRequestBean requestBean = new SignUpRequestBean(
                    Objects.requireNonNull(mEditTextEmail.getText()).toString().trim(),
                    Objects.requireNonNull(mEditTextUserName.getText()).toString().trim(),
                    Objects.requireNonNull(mEditTextPhoneNumber.getText()).toString().trim(),
                    Objects.requireNonNull(mEditTextPassword.getText()).toString().trim());
            callSignUpAPI(requestBean);
        }
    }

    /**
     * Validate all fields before signup
     * @return
     */
    private boolean validateAllFields() {
        boolean isValid = false;
        if(!TextUtils.isEmpty(mEditTextEmail.getText())){
            if(Helper.isValidEmail(mEditTextEmail.getText().toString())){
                if(!TextUtils.isEmpty(mEditTextPassword.getText())){
                    if(!TextUtils.isEmpty(mEditTextConfirmPassword.getText())){
                        if(mEditTextPassword.getText().toString().equalsIgnoreCase(
                                mEditTextConfirmPassword.getText().toString())){
                            if(!TextUtils.isEmpty(mEditTextUserName.getText())){
                                if(!TextUtils.isEmpty(mEditTextPhoneNumber.getText())) {
                                    isValid = true;
                                } else {
                                    Helper.showSnackBar(getString(R.string.enter_valid_phone_number),mRootView);
                                }                            }else {
                                Helper.showSnackBar(getString(R.string.username_empty),mRootView);
                            }
                        }else {
                            Helper.showSnackBar(getString(R.string.password_mismatch),mRootView);
                        }
                    }else {
                        Helper.showSnackBar(getString(R.string.enter_confirm_password),mRootView);
                    }
                }else {
                    Helper.showSnackBar(getString(R.string.enter_password),mRootView);
                }
            }else {
                Helper.showSnackBar(getString(R.string.enter_valid_email),mRootView);
            }
        }else {
            Helper.showSnackBar(getString(R.string.enter_email),mRootView);
        }
        return isValid;
    }


    private void callSignUpAPI(SignUpRequestBean requestBean) {
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.signUp(requestBean);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response != null && response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            handleSignUpResponse();
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
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }
    }

    private void handleSignUpResponse() {
        Helper.showSnackBar(getString(R.string.signed_up_successfully), mRootView);
        getActivity().onBackPressed();
    }

}
