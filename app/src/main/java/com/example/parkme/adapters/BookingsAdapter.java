package com.example.parkme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkme.R;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.models.BookingModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder>  {

    private List<BookingModel> mBookingModelList;
    private Context mContext;

    public BookingsAdapter(List<BookingModel> bookingModelList) {
        this.mBookingModelList = bookingModelList;
    }

    @NonNull
    @Override
    public BookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        mContext = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.bookings_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder holder, int position) {
        holder.mTextViewVehicleNumber.setText(mBookingModelList.get(position).getVehicleNumber());
        holder.mTextViewBookingDate.setText(mBookingModelList.get(position).getBookedAt());
        holder.mTextViewBookingNumber.setText(mBookingModelList.get(position).getBookingNumber());
        holder.mTextViewDestinationName.setText(mBookingModelList.get(position).getDestinationName());
        holder.mTextViewBookingStatus.setText(mBookingModelList.get(position).getBookingStatus());
    }

    @Override
    public int getItemCount() {
        return mBookingModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_vehicle_number)
        TextView mTextViewVehicleNumber;
        @BindView(R.id.tv_booking_number)
        TextView mTextViewBookingNumber;
        @BindView(R.id.tv_booked_on)
        TextView mTextViewBookingDate;
        @BindView(R.id.tv_destination)
        TextView mTextViewDestinationName;
        @BindView(R.id.layout_booking)
        LinearLayout mLayoutBooking;
        @BindView(R.id.booking_status)
        TextView mTextViewBookingStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
