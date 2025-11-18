package com.demo.dietiestates25.utils;

import com.google.android.material.textfield.TextInputEditText;

public final class ViewUtils {

    private ViewUtils() { }

    public static String getStringFromEditText(TextInputEditText txtInEditText) {
        return String.valueOf(txtInEditText.getText());
    }
}