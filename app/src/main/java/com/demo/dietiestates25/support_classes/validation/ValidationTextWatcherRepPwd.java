package com.demo.dietiestates25.support_classes.validation;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.textfield.TextInputLayout;

public class ValidationTextWatcherRepPwd implements TextWatcher {
    private final EditText editText;
    private final EditText pwdEditText;
    private final Utility utility;

    public ValidationTextWatcherRepPwd(EditText editText, EditText pwdEditText, Utility utility) {
        this.editText = editText;
        this.pwdEditText = pwdEditText;
        this.utility = utility;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not necessary
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not necessary
    }

    @Override
    public void afterTextChanged(Editable editable) {
        validate(editable.toString());
    }

    private void validate(String repeatPwd) {
        String pwd = pwdEditText.getText().toString();
        TextInputLayout txtInLayout = (TextInputLayout) editText.getParent().getParent();

        boolean isValid = repeatPwd.equals(pwd);

        if(isValid) {
            Validation.colorHintValidation(txtInLayout, Color.GREEN);
        }
        else {
            Validation.colorHintValidation(txtInLayout, Color.RED);
        }

        if (pwd.isEmpty()) {
            Validation.fieldEmptyCheck(txtInLayout, utility);
        }
    }
}
