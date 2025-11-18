package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.demo.dietiestates25.R;

public class ViewFilterHelper {
    private final Activity activity;
    private static final int CLASSIFY_PROPERTY_LAYOUT = R.layout.content_spinner;
    private static final int PRICE_LAYOUT = R.layout.content_seekbar_and_edittext;
    private static final int ROOMS_LAYOUT = R.layout.content_picker_and_switch;
    private static final int ENERGY_CLASS_LAYOUT = R.layout.content_picker;
    private static final int POSITION_LAYOUT = R.layout.content_edittext;
    private static final int RADIUS_LAYOUT = R.layout.content_radius_picker_and_switch;
    private View listingTypeFilterV;
    private View priceFilterV;
    private View roomsFilterV;
    private View energyFilterV;
    private View positionFilterV;
    private View radiusFilterV;

    public void setClassifyPropertyFilterV() { this.listingTypeFilterV = setViewFilter(CLASSIFY_PROPERTY_LAYOUT); }

    public void setPriceFilterV() { this.priceFilterV = setViewFilter(PRICE_LAYOUT); }

    public void setRoomsFilterV() { this.roomsFilterV = setViewFilter(ROOMS_LAYOUT); }

    public void setEnergyFilterV() { this.energyFilterV = setViewFilter(ENERGY_CLASS_LAYOUT); }

    public void setPositionFilterV() { this.positionFilterV = setViewFilter(POSITION_LAYOUT); }

    public void setRadiusFilterV() { this.radiusFilterV = setViewFilter(RADIUS_LAYOUT); }

    public View getClassifyPropertyFilterV() { return listingTypeFilterV; }

    public View getPriceFilterV() { return priceFilterV; }

    public View getRoomsFilterV() { return roomsFilterV; }

    public View getEnergyFilterV() { return energyFilterV; }

    public View getPositionFilterV() { return positionFilterV; }

    public View getRadiusFilterV() { return radiusFilterV; }

    public ViewFilterHelper(Activity activity) {
        this.activity = activity;
    }

    private View setViewFilter(int layoutFilterId) {
        LayoutInflater inflater = activity.getLayoutInflater();
        return FilterUtils.getViewFilter(inflater, layoutFilterId);
    }
}
