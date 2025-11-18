package com.demo.dietiestates25.support_classes.validation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.textfield.TextInputLayout;
public class ValidationTextWatcher implements TextWatcher {
    private final ValidationCallback validationCallback;
    private final EditText editText;
    private final Context context;

    public ValidationTextWatcher(EditText editText, ValidationCallback validationCallback, Context context) {
        this.editText = editText;
        this.validationCallback = validationCallback;
        this.context = context;
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

    private void validate(String txt) {
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        Utility utility = new Utility(context);
        validationCallback.validate(txt, textInputLayout, utility);
    }

    public interface ValidationCallback {
        void validate(String txt, TextInputLayout txtInLayout, Utility utility);
    }
}
