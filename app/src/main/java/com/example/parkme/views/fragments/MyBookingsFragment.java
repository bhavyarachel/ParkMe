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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.adapters.BookingsAdapter;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.response.BookingsResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.RetrofitErrorHelper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.activities.HomeActivity;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBookingsFragment extends Fragment {

    @BindView(R.id.layout_root)
    LinearLayout mRootView;
    @BindView(R.id.rv_bookings)
    RecyclerView mRecyclerViewBookings;
    @BindView(R.id.tv_no_bookings_to_show)
    TextView mTextViewNoBookings;

    private ProgressDialog mProgressDialog;


    public MyBookingsFragment() {
        // Required empty public constructor
    }


    public static MyBookingsFragment newInstance() {
        MyBookingsFragment fragment = new MyBookingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        ((HomeActivity)getActivity()).setTitleText(getString(R.string.my_bookings));
        mProgressDialog = new ProgressDialog(getContext());
        callGetAllBookingsAPI();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void callGetAllBookingsAPI() {
        mProgressDialog.setMessage(getString(R.string.loading));
        Helper.showProgress(mProgressDialog);
        User user = UserTableHelper.getCurrentUser();
        if (Helper.isDeviceConnected(Objects.requireNonNull(getContext()))) {
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<BookingsResponse> call = apiInterface.getAllBookings(user.getSessionKey());
            call.enqueue(new Callback<BookingsResponse>() {
                @Override
                public void onResponse(Call<BookingsResponse> call, Response<BookingsResponse> response) {
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
                public void onFailure(Call<BookingsResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong), mRootView);
                }
            });
        } else {
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error), mRootView);
        }

    }

    private void handleBookingResponse(BookingsResponse bookingsResponse) {
        if (bookingsResponse.getBookingModelList().size()==0){
            mTextViewNoBookings.setVisibility(View.VISIBLE);
        }
        BookingsAdapter adapter = new BookingsAdapter(bookingsResponse.getBookingModelList());
        mRecyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewBookings.setAdapter(adapter);
    }


}
