package com.demo.dietiestates25.support_classes.validation.validation_default_pwd;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.default_admin_pwd_changer.PwdCriteriaTxtKeys;
import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.validation.Validation;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;

public class ValidationTextWatcherDefaultPwdChange implements TextWatcher {
    private final Context context;
    private final TextInputEditText pwdEditTxt;
    private final TextView pwdInfoTxt;
    private final LinearProgressIndicator pwdProgressBar;
    private final InputLayoutHelper layoutHelper;
    private final PwdCriteriaTxtKeys txtKeys;
    private static final int MIN_PWD_LENGTH = Validation.getMinPwdLength();
    private static final int MAX_PWD_LENGTH = Validation.getMaxPwdLength();

    public ValidationTextWatcherDefaultPwdChange(Context context, TextInputEditText pwdEditTxt, LinearProgressIndicator pwdProgressBar, TextView pwdInfoTxt, InputLayoutHelper layoutHelper, PwdCriteriaTxtKeys txtKeys) {
        this.context = new WeakReference<>(context).get();
        this.pwdEditTxt = pwdEditTxt;
        this.pwdProgressBar = pwdProgressBar;
        this.pwdInfoTxt = pwdInfoTxt;

        this.layoutHelper = layoutHelper;
        this.txtKeys = txtKeys;

        this.pwdEditTxt.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not used
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not used
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String pwd = editable.toString();

        if(context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Handler.createAsync(Looper.getMainLooper()).post(() -> validateNewDefPwd(pwd));
            }
            else {
                new Handler(Looper.getMainLooper()).post(() -> validateNewDefPwd(pwd));
            }
        }
    }

    private void validateNewDefPwd(String pwd) {
        PwdStrengthIndicatorHelper strengthIndicatorHelper = new PwdStrengthIndicatorHelper(pwd, MIN_PWD_LENGTH, MAX_PWD_LENGTH, layoutHelper, txtKeys);

        PwdStrengthResult pwdStrengthResult = strengthIndicatorHelper.resolveForceStateProgressUI();

        pwdProgressBar.setProgress(pwdStrengthResult.progress, true);
        pwdProgressBar.setIndicatorColor(ContextCompat.getColor(context, pwdStrengthResult.colorRes));
        pwdInfoTxt.setText(pwdStrengthResult.txtRes);

        pwdProgressBar.invalidate();
        pwdInfoTxt.invalidate();

        strengthIndicatorHelper.updateAllCriteriaTxtUi();
    }

    public void cleanup() { pwdEditTxt.removeTextChangedListener(this); }
}