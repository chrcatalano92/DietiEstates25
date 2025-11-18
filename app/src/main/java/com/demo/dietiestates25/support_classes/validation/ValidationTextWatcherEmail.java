package com.demo.dietiestates25.support_classes.validation;

import android.graphics.Color;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.textfield.TextInputLayout;

public class ValidationTextWatcherEmail implements ValidationTextWatcher.ValidationCallback {

    public void validate(String email, TextInputLayout txtInLayout, Utility utility) {
        if (email.isEmpty()) {
            Validation.fieldEmptyCheck(txtInLayout, utility);
        } else if (Validation.isNotValidEmail(email)) {
            Validation.colorHintValidation(txtInLayout, Color.RED);
            String txt = utility.getXMLString(R.string.email_error) + "\n" + utility.getXMLString(R.string.email_example);
            txtInLayout.setError(txt);
        } else {
            Validation.colorHintValidation(txtInLayout, Color.GREEN);
        }
    }
}