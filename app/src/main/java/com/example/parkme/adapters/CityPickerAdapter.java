package com.example.parkme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.parkme.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityPickerAdapter extends BaseAdapter {

    private List<String> mCityList;

    public CityPickerAdapter(List<String> cityList) {
        this.mCityList = cityList;
    }

    @Override
    public int getCount() {
        return mCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.spinner_city_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextViewCityName.setText(mCityList.get(position));
        return convertView;
    }

    /**
     * Static view holder class.
     */
    public static class ViewHolder {
        @BindView(R.id.city_name)
        TextView mTextViewCityName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
