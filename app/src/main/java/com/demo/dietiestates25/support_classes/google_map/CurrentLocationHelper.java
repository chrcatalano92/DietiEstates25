package com.demo.dietiestates25.support_classes.google_map;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.PermissionLocationManager;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

/**
 * Classe Manager per la gestione della geolocalizzazione.
 * La sua Sola Responsabilità (SRP) è ottenere la posizione GPS attuale dell'utente.
 * È una classe Helper che astrae tutta la complessità di:
 *  1. Controllare i permessi (tramite il 'PermissionLocationManager')
 *  2. Chiedere i permessi se mancano.
 *  3. Ottenere il 'FusedLocationProviderClient' di Google.
 *  4. Richiedere la posizione con alta precisione.
 *  5. Restituire il risultato (latitudine/longitudine) o un errore tramite un'interfaccia (la LocationCallback).
 */
public class CurrentLocationHelper {
    private final Activity activity;
    private final PermissionLocationManager permissionManager;
    private LocationCallback currentCallback;

    public interface LocationCallback {
        void onLocationResult(double latitude, double longitude);
        void onPermissionDenied();
        void onError(String msg);
    }

    public CurrentLocationHelper(Activity activity, PermissionLocationManager permissionManager) {
        this.activity = activity;
        this.permissionManager = permissionManager;
    }

    public void requestCurrentLocation(LocationCallback callback) {
        this.currentCallback = callback;

        permissionManager.requestLocationPermissions(activity, new PermissionLocationManager.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                fetchLocation(currentCallback);
            }

            @Override
            public void onPermissionDenied() {
                if(currentCallback != null) {
                    callback.onPermissionDenied();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void fetchLocation(LocationCallback callback) {
        if(!permissionManager.hasLocationPermissions()) {
            callback.onPermissionDenied();
            return;
        }

        FusedLocationProviderClient fusedLocationClient = getFusedLocationProviderClient();

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationTokenSource().getToken())
                .addOnSuccessListener(activity, location -> {
                    if(location != null) {
                        callback.onLocationResult(location.getLatitude(), location.getLongitude());
                    }
                    else {
                        String errorMsg = ResourceUtils.getXMLString(activity, R.string.error_settings_location);
                        callback.onError(errorMsg);
                    }
                })
                .addOnFailureListener(activity, e -> callback.onError(e.getMessage()));
    }

    private FusedLocationProviderClient getFusedLocationProviderClient() { return LocationServices.getFusedLocationProviderClient(activity); }
}
