package com.demo.dietiestates25.support_classes.manage_spinner;

import android.widget.Spinner;

public class ManagementSpinnerCheckStatusHelper {
    private final Spinner provinceSpinner;
    private final Spinner citySpinner;
    private final Spinner titlePropertySpinner;

    public ManagementSpinnerCheckStatusHelper(Spinner provinceSpinner, Spinner citySpinner, Spinner titlePropertySpinner) {
        this.provinceSpinner = provinceSpinner;
        this.citySpinner = citySpinner;
        this.titlePropertySpinner = titlePropertySpinner;
    }

    public void resetSpinnerSelection(Spinner spinner) { spinner.setSelection(0); }

    public void spinnerState(Spinner spinner, boolean state) { spinner.setEnabled(state); }

    public void disableSpinnersFrom(int level) {
        if(level <= 4) {
            spinnerState(titlePropertySpinner, false);
            resetSpinnerSelection(titlePropertySpinner);
        }
        if(level <= 3) {
            spinnerState(citySpinner, false);
            resetSpinnerSelection(citySpinner);
        }
        if(level <= 2) {
            spinnerState(provinceSpinner, false);
            resetSpinnerSelection(provinceSpinner);
        }
    }
}
