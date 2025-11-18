package com.demo.dietiestates25.fragments_main_activity;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.demo.dietiestates25.utils.LocaleUtils;
import com.demo.dietiestates25.support_classes.PermissionLocationManager;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.LocationHelper;
import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.ThemeHelper;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String CLASS_TAG = "SettingsFragment";
    private DialogManager dialogManager;
    private SwitchPreference locationPref;
    private PermissionLocationManager permissionManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        dialogManager = new DialogManager(requireContext());
        permissionManager = new PermissionLocationManager(requireActivity(), dialogManager);
        locationPref = getPreferenceManager().findPreference(getString(R.string.settings_location_key));

        setupThemePreference(dialogManager);
        setupLanguagePreference(dialogManager);

        setupLocationPreference(requireContext());
    }

    private void setupThemePreference(DialogManager dialogManager) {
        Preference themePref = getPreferenceManager().findPreference(getString(R.string.settings_theme_key));

        if(themePref != null) {
            themePref.setOnPreferenceChangeListener(((preference, newValue) -> {
                ThemeHelper.applyTheme((String) newValue, requireContext());
                return true;
            }));
        }
        else {
            dialogManager.showErrorDialog(CLASS_TAG, R.string.settings_theme_error);
        }
    }

    private void setupLanguagePreference(DialogManager dialogManager) {
        Preference langPref = getPreferenceManager().findPreference(getString(R.string.settings_lang_key));

        if(langPref != null) {
            langPref.setOnPreferenceChangeListener(((preference, newValue) -> {
                LocaleUtils.applyLanguage(requireActivity(), (String) newValue);
                return true;
            }));
        }
        else {
            dialogManager.showErrorDialog(CLASS_TAG, R.string.settings_lang_error);
        }
    }

    private void setupLocationPreference(Context context) {
        if (locationPref != null) {
            updateLocationPreferenceState(locationPref);

            locationPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean enableLocation = (boolean) newValue;

                if(!enableLocation) {
                    locationDisable(context);
                    return true;
                }

                return locationManager();
            });
        }
    }

    private boolean locationManager() {
        if(shouldShowPermissionDeniedError()) {
            permissionManager.showPermissionDeniedError();
            return true;
        }
        if(LocationHelper.hasLocationPermissions(requireActivity())) {
            LocationHelper.startUsingLocation(dialogManager);
            permissionManager.resetDenialCount();
            return true;
        }
        else if(LocationHelper.hasCoarseLocationPermission(requireActivity())) {
            permissionManager.launchPermissionRequest();

            if(!LocationHelper.hasFineLocationPermission(requireActivity())) {
                permissionManager.incrementDenialCount();
            }
            return permissionManager.hasLocationPermissions();
        }
        else {
            permissionManager.launchPermissionRequest();
            permissionManager.incrementDenialCount();
            return false;
        }
    }

    private boolean shouldShowPermissionDeniedError() {
        return permissionManager.getDenialCount() >= 2 && !permissionManager.hasLocationPermissions();
    }

    private static void locationDisable(Context context) {
        LocationHelper.stopLocationServices(context);
    }

    private void updateLocationPreferenceState(SwitchPreference locationPref) {
        boolean hasPermissions = LocationHelper.hasLocationPermissions(requireActivity());
        locationPref.setChecked(hasPermissions);
        locationPref.setEnabled(true);
    }
}