package com.example.parkme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkme.R;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.models.VehicleModel;
import com.example.parkme.utils.AppHelper;
import com.example.parkme.views.fragments.EditVehicleFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

    List<VehicleModel> mVehicleModelList;
    Context mContext;

    public VehicleListAdapter(List<VehicleModel> vehicleModelList) {
        this.mVehicleModelList = vehicleModelList;
    }

    @NonNull
    @Override
    public VehicleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        mContext = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.vehicle_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleListAdapter.ViewHolder holder, int position) {
        holder.mTextViewVehicleNumber.setText(mVehicleModelList.get(position).getVehicleNumber());
        if(mVehicleModelList.get(position).getVehicleType() == AppConstants.four_wheeler){
            holder.mTextViewVehicleType.setText(AppConstants.FOUR_WHEELER);
        } else {
            holder.mTextViewVehicleType.setText(AppConstants.TWO_WHEELER);
        }
        holder.mTextViewEditVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppHelper.addFragment(mContext,
                        EditVehicleFragment.newInstance(
                                mVehicleModelList.get(position)),true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicleModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_vehicle_number)
        TextView mTextViewVehicleNumber;
        @BindView(R.id.tv_vehicle_type)
        TextView mTextViewVehicleType;
        @BindView(R.id.tv_edit)
        TextView mTextViewEditVehicle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
