package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.R;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

public class FilterLayoutValidator {
    FilterLayoutValidator(){}

    public static class ValidationRoomsValueResult {
        private final int rooms;
        private final boolean moreThanTenRooms;

        public ValidationRoomsValueResult(int rooms, boolean moreThanTenRooms) {
            this.rooms = rooms;
            this.moreThanTenRooms = moreThanTenRooms;
        }

        public int getRooms() { return rooms; }
        public boolean isMoreThanTenRooms() { return moreThanTenRooms; }
    }

    public static ValidationRoomsValueResult validateRoomsValue(MyViewModel filterData, View filterVRooms) {
        NumberPicker numPicker = filterVRooms.findViewById(R.id.numberPicker);
        MaterialSwitch matSwitch = filterVRooms.findViewById(R.id.matSwitch);

        int rooms = (filterData.getRooms() != null && filterData.getRooms().getValue() != null) ? filterData.getRooms().getValue() : 0;
        boolean moreThanTenRooms = (filterData.getMoreThanTenRooms() != null && filterData.getMoreThanTenRooms().getValue() != null) ? filterData.getMoreThanTenRooms().getValue() : Boolean.FALSE;

        if(rooms > 0 && moreThanTenRooms) {
            invalidateRoomsValues(filterData);
            clearNumberPicker(numPicker);
            clearMaterialSwitch(matSwitch);

            return new ValidationRoomsValueResult(0, false);
        }
        return new ValidationRoomsValueResult(rooms, moreThanTenRooms);
    }

    public static void validateRadiusValue(MyViewModel filterData, View filterVRadius, String radius) {
        NumberPicker stringPicker = filterVRadius.findViewById(R.id.stringPicker);
        MaterialSwitch matSwitch = filterVRadius.findViewById(R.id.matSwitch);

        if(stringPicker.getValue() == 0) { clearMaterialSwitch(matSwitch); }

        if(radius != null && radius.equals("-")) { invalidateRadiusValues(filterData); }
    }

    public static Double validatePriceValue(MutableLiveData<Integer> pickerValue, MutableLiveData<Double> fieldValue, MyViewModel filterData, View filterVPrices) {
        AppCompatSeekBar seekBar = filterVPrices.findViewById(R.id.seekbar);
        TextInputEditText doubleInpEditText = filterVPrices.findViewById(R.id.priceEditTxt);

        invalidatePriceValuePickerIsEmpty(filterData, seekBar);

        if(pickerValue == null && fieldValue == null) return 0.0;

        Integer pickerPrice = pickerValue != null ? pickerValue.getValue() : null;
        Double fieldPrice = fieldValue != null ? fieldValue.getValue() : null;

        if(pickerPrice != null && fieldPrice != null) {
            invalidatePriceValues(filterData);
            clearPickerPrice(seekBar);
            clearFieldPriceEditText(doubleInpEditText);
            return 0.0;
        }
        else if(pickerPrice != null) { return pickerPrice.doubleValue(); }
        else if(fieldPrice != null) { return fieldPrice; }
        else { return 0.0; }
    }

    private static void invalidatePriceValuePickerIsEmpty(MyViewModel filterData, AppCompatSeekBar seekBar) { if(seekBar.getProgress() == 0) { filterData.setPickerPrice(null); } }

    private static void invalidatePriceValues(MyViewModel filterData) {
        filterData.setFieldPrice(null);
        filterData.setPickerPrice(null);
    }

    private static void clearPickerPrice(AppCompatSeekBar seekBar) {
        seekBar.setProgress(0);
        seekBar.clearFocus();
    }

    private static void clearFieldPriceEditText(TextInputEditText doubleInpEditText) {
        if(doubleInpEditText != null) {
            doubleInpEditText.post(() -> {
                doubleInpEditText.setText("");
                doubleInpEditText.clearFocus();
            });
        }
    }

    private static void invalidateRadiusValues(MyViewModel filterData) {
        filterData.setRadius(null);
        filterData.setAccuratePosition(false);
    }

    private static void invalidateRoomsValues(MyViewModel filterData) {
        filterData.setRooms(0);
        filterData.setMoreThanTenRooms(false);
    }

    private static void clearNumberPicker(NumberPicker numPicker) {
        numPicker.setValue(0);
        numPicker.clearFocus();
        numPicker.setEnabled(true);
        numPicker.invalidate();
    }

    private static void clearMaterialSwitch(MaterialSwitch matSwitch) {
        matSwitch.setChecked(false);
        matSwitch.clearFocus();
        matSwitch.setEnabled(true);
        matSwitch.invalidate();
    }
}
