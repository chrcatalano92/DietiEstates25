package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.dietiestates25.R;

import java.util.List;

public class FilterAdapter extends ArrayAdapter<Filter> {
    private final LayoutInflater inflater;

    public FilterAdapter(@NonNull Context context, @NonNull List<Filter> filters) {
        super(context, 0, filters);

        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Filter filter = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.filters_advanced_search, parent, false);
        }

        if(filter != null) {
            TextView tvName = convertView.findViewById(R.id.item_name);
            tvName.setText(filter.getName());
        }

        return convertView;
    }
}
