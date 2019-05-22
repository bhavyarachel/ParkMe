package com.example.parkme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.models.StationModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationPickerAdapter extends BaseAdapter {

    private List<StationModel> mStationModelList;

    public LocationPickerAdapter(List<StationModel> mStationModelList) {
        this.mStationModelList = mStationModelList;
    }

    /**
     * Static view holder class.
     */
    public static class ViewHolder {
        @BindView(R.id.location_name)
        TextView mTextViewLocationName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return mStationModelList.size();
    }

    @Override
    public Object getItem(int position) {
        if(mStationModelList.size()>0)
            return mStationModelList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mStationModelList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.spinner_location_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextViewLocationName.setText(mStationModelList.get(position).getDestinationName());
        return convertView;
    }
}
