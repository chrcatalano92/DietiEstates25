package com.demo.dietiestates25.support_classes.validation;

import android.graphics.Color;

import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.textfield.TextInputLayout;

public class ValidationTextWatcherGeneralData implements ValidationTextWatcher.ValidationCallback {

    public void validate(String email, TextInputLayout txtInLayout, Utility utility) {
        if (email.isEmpty()) {
            Validation.fieldEmptyCheck(txtInLayout, utility);
        } else {
            Validation.colorHintValidation(txtInLayout, Color.GREEN);
        }
    }
}