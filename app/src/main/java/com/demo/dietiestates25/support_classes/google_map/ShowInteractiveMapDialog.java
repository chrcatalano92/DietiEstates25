package com.demo.dietiestates25.support_classes.google_map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.PermissionLocationManager;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.nominatim_service.NominatimSetup;

import com.demo.dietiestates25.utils.GoogleMapsUtils;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Classe Manager che mostra una mappa dentro un dialogo, a differenza di 'MapPointClickedActivity' che è un'intera schermata.
 * Serve a visualizzare una lista di indirizzi su una mappa e (opzionalmente) mostrare la posizione attuale dell'utente su quella stessa mappa.
 *  1. RICEVE DATI: Prende una lista di indirizzi (List<String>) e un nome di città (city) nel suo costruttore.
 *  2. MOSTRA DIALOGO: Crea un dialogo personalizzato che contiene un fragment di Google Maps.
 *  3. PLOTTA INDIRIZZI: Quando la mappa è pronta (onMapReady), cicla sulla lista di indirizzi e usa LocationHelper per convertirli in coordinate e aggiungerli come marker.
 *  4. CENTRA MAPPA: Centra la mappa sulla city fornita (o sull'Italia se city è null).
 *  5. MOSTRA POSIZIONE UTENTE: Chiama CurrentLocationHelper per ottenere la posizione dell'utente e la mostra come un cerchio lampeggiante (addBlinkingCircle).
 * La sua Sola Responsabilità (SRP) è ottenere la posizione GPS attuale dell'utente.
 */
public class ShowInteractiveMapDialog extends Dialog implements OnMapReadyCallback {
    private final Context context;
    private final DialogManager dialogManager;
    private GoogleMap gMap;
    private final List<String> addresses;
    private final String city;
    private final FragmentActivity activity;
    private final PermissionLocationManager permissionManager;

    public ShowInteractiveMapDialog(Context context, List<String> addresses, String city, PermissionLocationManager permissionManager) {
        super(context);

        this.context = context;
        this.dialogManager = new DialogManager(context);
        this.activity = (FragmentActivity) context;
        this.addresses = addresses;
        this.city = city;
        this.permissionManager = permissionManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = ResourceUtils.getXMLString(context, R.string.intMapTitle);
        MDialogManagementHelper.mapDialog(this, title, activity, gMap);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        this.gMap = gMap;
        int zoom = 10;

        for(String address : addresses) { // Add only markers on map
            LocationHelper.getLocationFromAddress(dialogManager, getContext(), gMap, address, activity, zoom,false);
        }

        if(city != null) { // Center map on specific city or region
            NominatimSetup.geocodeAddressRetrofit(context, dialogManager, gMap, city, zoom,false,false);
        }
        else { GoogleMapsUtils.defaultFocusMapOnItaly(gMap); }

        requestLocation(gMap);
    }

    private void requestLocation(@NonNull GoogleMap gMap) {
        GoogleMapsUtils.enableMyGMapLocation(context, gMap);

        CurrentLocationHelper.LocationCallback callback = new CurrentLocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                LatLng currUserLocation = LocationHelper.getLocation(latitude, longitude);
                GoogleMapsUtils.addBlinkingCircle(context, dialogManager, currUserLocation, gMap);
            }

            @Override
            public void onPermissionDenied() {
                String errorMsg = ResourceUtils.getXMLString(context, R.string.err_permission_location_denied);
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        };

        if(permissionManager != null) {
            CurrentLocationHelper locationHelper = new CurrentLocationHelper(activity, permissionManager);
            locationHelper.requestCurrentLocation(callback);
        }
        else {
            Toast.makeText(context, "Permission manager not available", Toast.LENGTH_SHORT).show();
        }
    }
}
