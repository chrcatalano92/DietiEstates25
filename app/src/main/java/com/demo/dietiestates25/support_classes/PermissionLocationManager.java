package com.demo.dietiestates25.support_classes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.app.ActivityCompat;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.LocationHelper;

/**
 * Classe Helper.
 * ALTA COESIONE (SRP): La sua Sola Responsabilità è gestire la complessità dei permessi di localizzazione di Android.
 * Astrae la logica di:
 *  1. Controllare se i permessi esistono (hasLocationPermissions).
 *  2. Chiederli (requestLocationPermissions).
 *  3. Gestire la logica della RATIONALE (spiegare all'utente perché servono, checkRationaleAndLunch).
 *  4. Tracciare i rifiuti (incrementDenialCount) per non chiederli all'infinito.
 *  BASSO ACCOPPIAMENTO: Comunica con il chiamante tramite un'interfaccia (PermissionCallback).
 */
public class PermissionLocationManager {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String PREF_NAME = "PermissionPrefs";
    private static final String KEY_DENIAL_COUNT = "location_denial_count";
    private final Activity activity;
    private final DialogManager dialogManager;
    private PermissionCallback callback;
    private final SharedPreferences prefs;

    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    public PermissionLocationManager(Activity activity, DialogManager dialogManager) {
        this.activity = activity;
        this.dialogManager = dialogManager;
        this.prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasLocationPermissions() { return LocationHelper.hasFineLocationPermission(activity) && LocationHelper.hasCoarseLocationPermission(activity); }

    public void requestLocationPermissions(Activity activity, PermissionCallback callback) {
        this.callback = callback;

        if(hasLocationPermissions()) {
            resetDenialCount();
            if(callback != null) {
                callback.onPermissionGranted();
            }
            return;
        }

        boolean hasSomePermission = LocationHelper.hasCoarseLocationPermission(activity) || LocationHelper.hasFineLocationPermission(activity);

        if(hasSomePermission) {
            launchPermissionRequest();
        }
        else {
            checkRationaleAndLaunch(activity);
        }
    }

    private void checkRationaleAndLaunch(Activity activity) {
        if(activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

            incrementDenialCount();

            if(getDenialCount() < 2) {
                dialogManager.showInfoDialog(R.string.settings_location_denied_title, R.string.settings_location_denied, this::launchPermissionRequest);
            }

            notifyPermissionDenied();
        }
        else {
            launchPermissionRequest();
        }
    }

    private void notifyPermissionDenied() {
        if(callback != null) { callback.onPermissionDenied(); }
    }

    public void showPermissionDeniedError() {
        dialogManager.showErrorDialog("PermissionLocationManager", R.string.err_permission_location_denied);
    }

    public void incrementDenialCount() {
        int currentCount = getDenialCount();
        prefs.edit().putInt(KEY_DENIAL_COUNT, currentCount+1).apply();
    }

    public void resetDenialCount() { prefs.edit().putInt(KEY_DENIAL_COUNT, 0).apply(); }

    public int getDenialCount() { return prefs.getInt(KEY_DENIAL_COUNT, 0); }

    public void launchPermissionRequest() {
        ActivityCompat.requestPermissions(activity, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, LOCATION_PERMISSION_REQUEST_CODE);
    }
}
