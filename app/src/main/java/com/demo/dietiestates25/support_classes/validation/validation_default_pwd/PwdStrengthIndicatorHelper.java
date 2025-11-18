package com.demo.dietiestates25.support_classes.validation.validation_default_pwd;

import android.widget.TextView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.default_admin_pwd_changer.PwdCriteriaTxtKeys;
import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.validation.PwdValidateHelper;

public class PwdStrengthIndicatorHelper {
    private final String pwd;
    private final int minPwdLength;
    private final int maxPwdLength;
    private final PwdValidateHelper pwdValidateHelper;
    private final TextView criteriaTxtLength;
    private final TextView criteriaTxtUppercase;
    private final TextView criteriaTxtLowercase;
    private final TextView criteriaTxtNumber;
    private final TextView criteriaSpecialChars;
    private final int pwdLen;

    public PwdStrengthIndicatorHelper(String pwd, int minPwdLength, int maxPwdLength, InputLayoutHelper layoutHelper, PwdCriteriaTxtKeys txtKeys) {
        this.pwd = pwd;
        this.minPwdLength = minPwdLength;
        this.maxPwdLength = maxPwdLength;

        this.criteriaTxtLength = layoutHelper.getTextViewId(txtKeys.getCriteriaTxtLength());
        this.criteriaTxtUppercase = layoutHelper.getTextViewId(txtKeys.getCriteriaTxtUppercase());
        this.criteriaTxtLowercase = layoutHelper.getTextViewId(txtKeys.getCriteriaTxtLowercase());
        this.criteriaTxtNumber = layoutHelper.getTextViewId(txtKeys.getCriteriaTxtNumber());
        this.criteriaSpecialChars = layoutHelper.getTextViewId(txtKeys.getCriteriaTxtSpecialChars());

        pwdLen = pwd.length();

        pwdValidateHelper = new PwdValidateHelper(pwd);
    }

    public PwdStrengthResult resolveForceStateProgressUI() {
        if(pwd.isEmpty()) {
            resetAllCriteriaTxtUi();

            return new PwdStrengthResult(0, R.color.pwd_strength_default, R.string.pwd_strength_default);
        }

        int strengthScore = pwdRulesCount();

        if(pwdLen > maxPwdLength) {
            return new PwdStrengthResult(100, R.color.pwd_strength_terrible, R.string.pwd_too_long);
        }

        if(pwdLen == maxPwdLength && pwdValidateHelper.checkAllPwdRules()) {
            return new PwdStrengthResult(100, R.color.pwd_strength_excellent, R.string.pwd_strength_excellent);
        }

        if(pwdLen >= minPwdLength && pwdValidateHelper.checkAllPwdRules()) {
            return new PwdStrengthResult(75, R.color.pwd_strength_strong, R.string.pwd_strength_strong);
        }

        if(strengthScore == 4 && pwdValidateHelper.checkAllPwdRules()) {
            return new PwdStrengthResult(60, R.color.pwd_strength_good, R.string.pwd_strength_good);
        }

        if(strengthScore == 3) {
            return new PwdStrengthResult(50, R.color.pwd_strength_fair, R.string.pwd_strength_fair);
        }

        if(strengthScore == 2) {
            return new PwdStrengthResult(20, R.color.pwd_strength_weak, R.string.pwd_strength_weak);
        }

        return new PwdStrengthResult(10, R.color.pwd_strength_terrible, R.string.pwd_strength_terrible);
    }

    private int pwdRulesCount() {
        int score = 0;

        if(pwdValidateHelper.isHasUpperCase()) { score++; }
        if(pwdValidateHelper.isHasLowerCase()) { score++; }
        if(pwdValidateHelper.isHasNumber()) { score++; }
        if(pwdValidateHelper.isHasLeast2SpecialCharacters()) { score++; }

        return score;
    }

    public void updateAllCriteriaTxtUi() {
        updateCriteriaTxtV(criteriaTxtLength, pwdLen >= minPwdLength && pwdLen <= maxPwdLength);

        if(pwdValidateHelper.isHasUpperCase()) { updateCriteriaTxtV(criteriaTxtUppercase, true); }
        if(pwdValidateHelper.isHasLowerCase()) { updateCriteriaTxtV(criteriaTxtLowercase, true); }
        if(pwdValidateHelper.isHasNumber()) { updateCriteriaTxtV(criteriaTxtNumber, true); }
        if(pwdValidateHelper.isHasLeast2SpecialCharacters()) { updateCriteriaTxtV(criteriaSpecialChars, true); }
    }

    private void resetAllCriteriaTxtUi() {
        updateCriteriaTxtV(criteriaTxtLength, false);
        updateCriteriaTxtV(criteriaTxtUppercase, false);
        updateCriteriaTxtV(criteriaTxtLowercase, false);
        updateCriteriaTxtV(criteriaTxtNumber, false);
        updateCriteriaTxtV(criteriaSpecialChars, false);
    }

    private void updateCriteriaTxtV(TextView txtV, boolean isValid) {
        txtV.setSelected(isValid);
    }
}
