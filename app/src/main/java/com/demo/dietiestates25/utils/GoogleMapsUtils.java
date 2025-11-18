package com.demo.dietiestates25.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.core.content.ContextCompat;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Classe Utils STATICA per centralizzare le operazioni comuni su un oggetto GoogleMap.
 * SRP: La sua Sola Responsabilità è Manipolare la Mappa (aggiungere Marker, muovere la camera, pulire la mappa).
 */
public final class GoogleMapsUtils {

    public static final String CLASS_TAG = "GoogleMapUtils";

    private GoogleMapsUtils(){}

    /* Stato per la gestione del cerchio lampeggiante (blinking) */
    private static Circle locationCircle;
    private static Marker userPositionMarker;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static Runnable blinkingRunnable;

    /**
     * Centra la mappa su una vista predefinita dell'Italia.
     */
    public static void defaultFocusMapOnItaly(GoogleMap googleMap) {
        LatLng italy = LocationHelper.getItalyLocation();
        moveCamera(googleMap, italy, 6);
    }

    /**
     * Aggiunge un Marker generico sulla mappa.
     */
    public static void addMarker(GoogleMap gMap, String title, LatLng location, BitmapDescriptor icon) {
        if(gMap != null) {
            gMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(title)
                    .icon(icon)
            );
        }
    }

    /**
     * Muove la camera della mappa a una data posizione specifica e zoom.
     */
    public static void moveCamera(GoogleMap gMap, LatLng location, int zoom) {
        if(gMap != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        }
    }

    /**
     * Imposta un'icona personalizzata (BitmapDescriptor) per il Marker sulla mappa da una risorsa drawable.
     */
    public static BitmapDescriptor setIconMarker(int imgId) { return BitmapDescriptorFactory.fromResource(imgId); }

    /**
     * Abilita il PALLINO BLU della posizione utente sulla mappa, solo se i permessi sono già stati concessi.
     */
    public static void enableMyGMapLocation(Context context, GoogleMap gMap) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Aggiunge un cerchio lampeggiante per la posizione dell'utente.
     */
    public static void addBlinkingCircle(Context context, DialogManager dialogManager, LatLng lng, GoogleMap gMap) {
        clear(); // Pulisce il cerchio precedente

        CircleOptions circleOptions = new CircleOptions()
                .center(lng)
                .radius(50)
                .fillColor(Color.argb(90, 0, 0, 255))
                .strokeColor(Color.BLUE)
                .strokeWidth(2);

        locationCircle = gMap.addCircle(circleOptions);
        startBlinking();
        onCircleClick(context, dialogManager, gMap, locationCircle);
    }

    /**
     * Avvia l'effetto di lampeggiamento (blink) del cerchio.
     */
    public static void startBlinking() {
        blinkingRunnable = () -> {
            if(locationCircle != null) {
                locationCircle.setVisible(!locationCircle.isVisible());
                handler.postDelayed(blinkingRunnable, 500);
            }
        };
        handler.postDelayed(blinkingRunnable, 500);
    }

    /**
     * Ferma l'effetto di lampeggiamento e rimuove il cerchio e il marker.
     */
    public static void stopBlinking() {
        if(blinkingRunnable != null) {
            handler.removeCallbacks(blinkingRunnable);
        }
        if(locationCircle != null) {
            locationCircle.remove();
            locationCircle = null;
        }
        if(userPositionMarker != null) {
            userPositionMarker.remove();
            userPositionMarker = null;
        }
    }

    /**
     * Gestisce il click sul cerchio della posizione.
     */
    private static void onCircleClick(Context context, DialogManager dialogManager, GoogleMap gMap, Circle circle) {
        circle.setClickable(true);

        gMap.setOnCircleClickListener(clickedCircle -> {
            if(clickedCircle.equals(locationCircle)) {
                showInfoWindow(context, dialogManager, circle.getCenter(), gMap);
            }
        });
    }

    /**
     * Mostra una Finestra Informativa (Marker) sulla posizione dell'utente.
     */
    private static void showInfoWindow(Context context, DialogManager dialogManager, LatLng position, GoogleMap gMap) {
        if(userPositionMarker != null) {
            userPositionMarker.remove();
        }

        String title = ResourceUtils.getXMLString(context, R.string.your_position);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .icon(GoogleMapsUtils.setIconMarker(R.drawable.UsrPinMarker));

        userPositionMarker = gMap.addMarker(markerOptions);

        if(userPositionMarker != null) {
            userPositionMarker.showInfoWindow();
        }
        else {
            String errorMsg = ResourceUtils.getXMLString(context, R.string.marker_curr_pos_err);
            dialogManager.showErrorDialog(CLASS_TAG, errorMsg);
        }
    }

    /**
     * Pulisce tutti gli elementi gestiti da questa classe (cerchi, marker, handler)
     */
    public static void clear() {
        if(locationCircle != null) { locationCircle.remove(); }

        if(userPositionMarker != null) {
            userPositionMarker.remove();
            userPositionMarker = null;
        }

        if(blinkingRunnable != null) { handler.removeCallbacks(blinkingRunnable); }
    }

    /**
     * Rimuove tutti i marker, poligoni, ecc. dalla mappa.
     */
    public static void clearOldMarkers(GoogleMap googleMap) {
        if (googleMap != null) {
            googleMap.clear();
        }
    }
}
