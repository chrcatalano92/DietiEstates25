package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import android.text.Editable;
import android.text.TextWatcher;

import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.MyViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class EditTextHelper {
    private EditTextHelper(){}

    public static void cityEditText(TextInputEditText editText, MyViewModel model) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No used
            }

            @Override
            public void afterTextChanged(Editable e) {
                model.setCity(e.toString());
            }
        });
    }
}
