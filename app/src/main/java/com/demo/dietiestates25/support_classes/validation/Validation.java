package com.demo.dietiestates25.support_classes.validation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextWatcher;
import android.util.Patterns;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Validation {

    static int minPwdLength = 12;
    static int maxPwdLength = 20;

    private Validation(){}

    private static void correctValidation(TextInputLayout txtInLayout, int colorId) {
        errorState(txtInLayout,false);
        txtInLayout.setHelperTextColor(getColor(colorId));
    }

    private static void errorValidation(TextInputLayout txtInLayout, int colorId) {
        errorState(txtInLayout, true);
        txtInLayout.setErrorTextColor(getColor(colorId));
    }

    private static ColorStateList getColor(int colorId) {
        return ColorStateList.valueOf(colorId);
    }

    private static void errorState(TextInputLayout txtInLayout, boolean set) { txtInLayout.setErrorEnabled(set); }

    public static void colorHintValidation(TextInputLayout txtInLayout, int colorId) {
        int blueC = Color.GREEN;

        txtInLayout.setHintTextColor(getColor(colorId));

        if(colorId == blueC) {
            correctValidation(txtInLayout, colorId);
        }
        else {
            errorValidation(txtInLayout, colorId);
        }
    }

    public static boolean isNotValidEmail(String email) { return email != null && !Patterns.EMAIL_ADDRESS.matcher(email).matches(); }

    public static int getMinPwdLength() { return minPwdLength; }

    public static int getMaxPwdLength() {
        return maxPwdLength;
    }

    public static boolean isValidPwd(String pwd) {
        PwdValidateHelper validateHelper = new PwdValidateHelper(pwd);
        return pwd != null && !pwd.isEmpty() && ((pwd.length() >= minPwdLength) && (pwd.length() <= maxPwdLength) && validateHelper.checkAllPwdRules());
    }

    public static void fieldEmptyCheck(TextInputLayout txtInLayout, Utility utility) {
        colorHintValidation(txtInLayout, Color.RED);
        txtInLayout.setError(utility.getXMLString(R.string.field_required));
    }

    private static void startValidation(TextInputEditText txtInEditText, TextWatcher textWatcher) {
        txtInEditText.addTextChangedListener(textWatcher);
    }

    public static void generalValidation(TextInputEditText txtInEditText, Context context) {
        startValidation(txtInEditText, new ValidationTextWatcher(txtInEditText, new ValidationTextWatcherGeneralData(), context));
    }

    public static void emailValidation(TextInputEditText txtInEditText, Context context) {
        startValidation(txtInEditText, new ValidationTextWatcher(txtInEditText, new ValidationTextWatcherEmail(), context));
    }

    public static void pwdValidation(TextInputEditText txtInEditText, Context context) {
        startValidation(txtInEditText, new ValidationTextWatcher(txtInEditText, new ValidationTextWatcherPwd(), context));
    }

    public static void repPwdValidation(TextInputEditText pwdRep, TextInputEditText pwd, Utility utility) {
        startValidation(pwdRep, new ValidationTextWatcherRepPwd(pwdRep, pwd, utility));
    }
}
