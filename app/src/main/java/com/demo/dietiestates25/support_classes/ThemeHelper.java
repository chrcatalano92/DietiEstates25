package com.demo.dietiestates25.support_classes;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.demo.dietiestates25.R;

public final class ThemeHelper {
    private ThemeHelper(){}

    public static void applyTheme(String mode, Context context) {
        String darkValue = context.getString(R.string.settings_theme_value_dark);
        String lightValue = context.getString(R.string.settings_theme_value_light);

        if(darkValue.equals(mode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (lightValue.equals(mode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    public static boolean isNightMode() { return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == AppCompatDelegate.getDefaultNightMode(); }
}
