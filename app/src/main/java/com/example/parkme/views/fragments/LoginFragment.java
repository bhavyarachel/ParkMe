package com.example.parkme.views.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parkme.R;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.request.LoginRequestBean;
import com.example.parkme.api.response.LoginResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.utils.AppHelper;
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
public class LoginFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    @BindView(R.id.et_email)
    TextInputEditText mEditTextEmail;
    @BindView(R.id.et_password)
    TextInputEditText mEditTextPassword;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Returns an instance of LoginFragment
     *
     * @return
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
    }

    @OnClick({R.id.tv_sign_up})
    void onClickSignUp() {
        AppHelper.addFragment(getContext(), SignUpFragment.newInstance(),true);
    }

    @OnClick(R.id.btn_login)
    void onClickLogin() {
        if (validateLoginCredentials()) {
            mProgressDialog.setMessage(getString(R.string.loading));
            Helper.showProgress(mProgressDialog);
            LoginRequestBean requestBean = new LoginRequestBean(
                    Objects.requireNonNull(mEditTextEmail.getText()).toString().trim(),
                    Objects.requireNonNull(mEditTextPassword.getText()).toString().trim());
            callLoginApi(requestBean);
        }

    }

    /**
     * Validating email and password
     *
     * @return
     */
    private boolean validateLoginCredentials() {
        boolean isValid = false;
        if (!TextUtils.isEmpty(mEditTextEmail.getText())) {
            if (Helper.isValidEmail(mEditTextEmail.getText().toString())) {
                if (!TextUtils.isEmpty(mEditTextPassword.getText())) {
                    isValid = true;
                } else {
                    Helper.showSnackBar(getString(R.string.enter_password), mRootView);
                }
            } else {
                Helper.showSnackBar(getString(R.string.enter_valid_email), mRootView);
            }
        } else {
            Helper.showSnackBar(getString(R.string.enter_email), mRootView);
        }
        return isValid;
    }

    private void callLoginApi(LoginRequestBean requestBean) {
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.login(requestBean);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null)
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            handleLoginResponse(response.body());
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

    /**
     * Store the authorized user details in the database
     * Navigate to home screen
     * @param body login response body
     */
    private void handleLoginResponse(LoginResponse body) {
        String email = body.getUserModel().getEmail();
        String username = body.getUserModel().getName();
        String phone = body.getUserModel().getPhone();
        String session_key = body.getUserModel().getSessionKey();
        int userId = body.getUserModel().getUserId();
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setPhone(phone);
        user.setSessionKey(session_key);
        user.setUserName(username);
        user.setLoggedIn(true);
        user.save();
        Objects.requireNonNull(getActivity()).finish();
        Intent i = new Intent(getContext(), HomeActivity.class);
        startActivity(i);
    }

}
