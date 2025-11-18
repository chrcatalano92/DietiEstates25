package com.demo.dietiestates25.support_classes.google_map;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.nominatim_service.NominatimSetup;
import com.demo.dietiestates25.utils.GoogleMapsUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Classe Helper STATICA (final) per centralizzare tutta la logica relativa ai servizi di Localizzazione.
 * Gestisce i permessi, lo stop dei servizi, il geocoding (Address -> LatLng) e la creazione di coordinate LatLng.
 */
public final class LocationHelper {
    public static final String TAG = "LocationHelper";
    private static PendingIntent locationPendingIntent;

    private LocationHelper() {}

    public static boolean hasLocationPermissions(Activity activity) {
        return hasFineLocationPermission(activity) && hasCoarseLocationPermission(activity);
    }

    public static boolean hasFineLocationPermission(Activity activity) {
        return isGrantedPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean hasCoarseLocationPermission(Activity activity) {
        return isGrantedPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private static boolean isGrantedPermission(Context context, String permissionManifest) {
        return ContextCompat.checkSelfPermission(context, permissionManifest) == PackageManager.PERMISSION_GRANTED;
    }

    public static void stopLocationServices(Context context) { stopFusedLocationUpdates(context); }

    private static void stopFusedLocationUpdates(Context context) {
        try {
            if(isGooglePlayServicesAvailable(context)) {
                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
                client.removeLocationUpdates(locationPendingIntent)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Fused location updates stopped");
                            locationPendingIntent = null;
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "Failed to stop location updates", e));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error stopping fused location updates", e);
        }
    }

    private static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        return resultCode == ConnectionResult.SUCCESS;
    }

    public static void startUsingLocation(DialogManager dialogManager) {
        dialogManager.showInfoDialog(R.string.accurate_location);
    }

    public static void startUsingApproximateLocation(DialogManager dialogManager) {
        dialogManager.showInfoDialog(R.string.approximate_location);
    }

    /**
     * Esegue il Geocoding (converte un indirizzo in LatLng).
     * Usa il Geocoder nativo di Android 13+ (Tiramisu).
     * Esegue il fallback a Nominatim (via Retrofit) per le versioni precedenti.
     */
    public static void getLocationFromAddress(DialogManager dialogManager, Context context, GoogleMap gMap, String address, FragmentActivity activity, int zoom, boolean addMarkerAndMove) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            geocoder.getFromLocationName(address, 1, addresses ->
                    activity.runOnUiThread(() -> {
                        if(!addresses.isEmpty()) {
                            Address location = addresses.get(0);

                            LatLng lng = LocationHelper.getLocation(location.getLatitude(), location.getLongitude());

                            BitmapDescriptor icon = GoogleMapsUtils.setIconMarker(R.drawable.PropertyPinMarker);

                            GoogleMapsUtils.addMarker(gMap, address, lng, icon);
                        }
                        else {
                            dialogManager.showErrorDialog(TAG, R.string.show_map_error);
                        }
            }));
        }
        else {
            NominatimSetup.geocodeAddressRetrofit(context, dialogManager, gMap, address, zoom, addMarkerAndMove, true);
        }
    }

    /**
     * Ritorna un LatLng per il centro dell'Italia (usato come focus di default).
     */
    public static LatLng getItalyLocation() { return getLocation(41.8797400, 12.5697200); }

    /**
     * Metodo factory "pulito" (Clean Code) per creare un LatLng.
     */
    public static LatLng getLocation(double lat, double lon) { return new LatLng(lat, lon); }
}
