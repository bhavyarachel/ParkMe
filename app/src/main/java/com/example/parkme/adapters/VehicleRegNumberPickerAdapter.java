package com.example.parkme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.models.VehicleModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehicleRegNumberPickerAdapter extends BaseAdapter {

    private List<VehicleModel> mVehicleModelList;


    public VehicleRegNumberPickerAdapter(List<VehicleModel> vehicleModelList) {
        this.mVehicleModelList = vehicleModelList;
    }

    @Override
    public int getCount() {
        return mVehicleModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVehicleModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mVehicleModelList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.spinner_vehicle_reg_number, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextViewVehicleRegNumber.setText(mVehicleModelList.get(position).getVehicleNumber());
        return convertView;
    }

    /**
     * Static view holder class.
     */
    public static class ViewHolder {
        @BindView(R.id.tv_vehicle_number)
        TextView mTextViewVehicleRegNumber;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
