package com.demo.dietiestates25.support_classes.validation;

import static com.demo.dietiestates25.support_classes.validation.Validation.*;

import android.graphics.Color;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.textfield.TextInputLayout;

public class ValidationTextWatcherPwd implements ValidationTextWatcher.ValidationCallback {

    int minPwdLength = getMinPwdLength();
    int maxPwdLength = getMaxPwdLength();

    public void validate(String pwd, TextInputLayout txtInLayout, Utility utility) {
        int colorId = Color.RED;
        int colorId2 = Color.GREEN;

        if (pwd.isEmpty()) {
            fieldEmptyCheck(txtInLayout, utility);
        } else if (checkPwdRules(pwd, txtInLayout, utility) && (pwd.length() < minPwdLength)) {
            colorHintValidation(txtInLayout, colorId);
            txtInLayout.setError(utility.getXMLString(R.string.too_short_pwd));
        } else if (checkPwdRules(pwd, txtInLayout, utility) && (pwd.length() <= maxPwdLength)) {
            colorHintValidation(txtInLayout, colorId2);
            txtInLayout.setHelperText(utility.getXMLString(R.string.strong_pwd));
        } else if (!checkPwdRules(pwd, txtInLayout, utility)) {
            colorHintValidation(txtInLayout, colorId);
        } else if (pwd.length() > Validation.maxPwdLength) {
            colorHintValidation(txtInLayout, colorId);
            txtInLayout.setError(utility.getXMLString(R.string.pwd_too_long_label));
        } else {
            txtInLayout.setError(null);
            txtInLayout.setHelperText(null);
        }
        enableCounter(txtInLayout, Validation.maxPwdLength);
    }

    private void enableCounter(TextInputLayout txtInLayout, int maxLenghtChar) {
        txtInLayout.setCounterMaxLength(maxLenghtChar);
        txtInLayout.setCounterEnabled(true);
    }

    private boolean checkPwdRules(String pwd, TextInputLayout txtInLayout, Utility utility) {
        String fieldCheck = utility.getXMLString(R.string.field_check);
        String fieldCheck2 = utility.getXMLString(R.string.field_check2);
        String newParagraph = " ";
        PwdValidateHelper validateHelper = new PwdValidateHelper(pwd);

        if(!validateHelper.isHasUpperCase()) {
            txtInLayout.setError(fieldCheck + newParagraph + utility.getXMLString(R.string.uppercase_check));
            return false;
        }
        if(!validateHelper.isHasLowerCase()) {
            txtInLayout.setError(fieldCheck + newParagraph + utility.getXMLString(R.string.lowercase_check));
            return false;
        }
        if (!validateHelper.isHasNumber()) {
            txtInLayout.setError(fieldCheck + newParagraph + utility.getXMLString(R.string.number_check));
            return false;
        }
        if(!validateHelper.isHasLeast2SpecialCharacters()) {
            txtInLayout.setError(fieldCheck2 + newParagraph + utility.getXMLString(R.string.special_character_check));
            return false;
        }
        else {
            return true;
        }
    }
}
