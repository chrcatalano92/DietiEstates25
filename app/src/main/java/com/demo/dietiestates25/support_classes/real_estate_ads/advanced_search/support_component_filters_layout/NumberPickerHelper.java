package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import android.widget.ImageButton;
import android.widget.NumberPicker;

import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.MyViewModel;

public class NumberPickerHelper {
    private NumberPickerHelper(){}

    public static void numberPicker(NumberPicker numPicker, MyViewModel model) {
        numPicker.setMinValue(0);
        numPicker.setMaxValue(10);

        numPicker.setOnValueChangedListener((numberPicker, oldValue, newValue) -> model.setRooms(newValue));
    }

    public static void decrementPicker(ImageButton imgBtnDecrement, NumberPicker numPicker, MyViewModel model) {
        imgBtnDecrement.setOnClickListener(view -> {
            int currentValue = currentValuePicker(numPicker) - 1;

            if(currentValue > 0) {
                numPicker.setValue(currentValue);
                model.setRooms(currentValue);
            }
        });
    }

    public static void incrementPicker(ImageButton imgBtnIncrement, NumberPicker numPicker, MyViewModel model) {
        imgBtnIncrement.setOnClickListener(view -> {
            int currentValue = currentValuePicker(numPicker) + 1;

            if(currentValue <= 10) {
                numPicker.setValue(currentValue);
                model.setRooms(currentValue);
            }
        });
    }

    private static int currentValuePicker(NumberPicker numPicker) {
        return numPicker.getValue();
    }
}
