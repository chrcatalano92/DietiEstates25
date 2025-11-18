package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.demo.dietiestates25.R;

public class CustomSpinnerArrayAdapter extends ArrayAdapter<CharSequence> {
    private final int resourceId;

    public CustomSpinnerArrayAdapter(@NonNull Context context, int resourceId, @NonNull CharSequence[] objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;

        if(convertView == null) {
            view = inflater.inflate(resourceId, parent, false);
        }
        else {
            view = convertView;
        }

        TextView txtV = view.findViewById(android.R.id.text1);

        txtV.setText(getItem(position));

        if(position >= getCount() - 2) {
            txtV.setTextColor(getContext().getColor(android.R.color.darker_gray));
        }
        else {
            txtV.setTextColor(ContextCompat.getColor(getContext(), R.color.Text));
        }

        return view;
    }
}
