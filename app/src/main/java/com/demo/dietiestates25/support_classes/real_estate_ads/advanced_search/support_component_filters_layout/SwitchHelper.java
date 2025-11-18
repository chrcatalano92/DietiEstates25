package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import com.google.android.material.materialswitch.MaterialSwitch;

public class SwitchHelper {
    private SwitchHelper(){}

    public interface SwitchCallback {
        void switchHandler(boolean isChecked);
    }

    public static void materialSwitch(MaterialSwitch matSwitch, SwitchCallback listener) {
        matSwitch.setOnCheckedChangeListener(((compoundButton, isChecked) -> listener.switchHandler(isChecked)));
    }
}
