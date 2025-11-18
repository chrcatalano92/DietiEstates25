package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import android.widget.NumberPicker;

import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.MyViewModel;

public class StringPickerHelper {
    private StringPickerHelper(){}

    public static void energyClassPicker(NumberPicker stringPicker, String[] data, MyViewModel model) {
        initStringPicker(stringPicker, data);

        stringPicker.setOnValueChangedListener((numberPicker, oldValue, newValue) -> model.setEnergyClass(dataSelected(data, newValue)));
    }

    public static String[] energyClassData() { return new String[] {"-", "A4", "A3", "A2", "A1", "B", "C", "D", "E", "F", "G"}; }

    public static void radiusPicker(NumberPicker stringPicker, String[] data, MyViewModel model) {
        initStringPicker(stringPicker, data);

        stringPicker.setOnValueChangedListener(((numberPicker, oldValue, newValue) -> model.setRadius(dataSelected(data, newValue))));
    }

    private static void initStringPicker(NumberPicker stringPicker, String[] data) {
        stringPicker.setMinValue(0);
        stringPicker.setMaxValue(data.length - 1);
        stringPicker.setDisplayedValues(data);
    }

    private static String dataSelected(String[] data, int newValue) {
        return data[newValue];
    }
}
