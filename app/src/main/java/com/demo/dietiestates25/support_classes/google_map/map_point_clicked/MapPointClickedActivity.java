package com.demo.dietiestates25.support_classes.google_map.map_point_clicked;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityMapPointClickedBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.LocationHelper;
import com.demo.dietiestates25.utils.GoogleMapsUtils;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClientHelper;
import com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper.RetrofitGetListHelper;
import com.demo.dietiestates25.utils.IntentUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

/**
 * Questa Activity gestisce una specifica funzionalità di ricerca avanzata:
 * la ricerca di immobili entro un raggio (radius) da un punto
 * personalizzato selezionato dall'utente su una mappa.
 * Implementa OnMapReadyCallback per gestire l'inizializzazione della mappa.
 */
public class MapPointClickedActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String CLASS_TAG = "MapPointClickedActivity";
    private RetrofitClientHelper clientHelper;
    private GoogleMap gMap;
    private int metersRadius;
    private DialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapPointClickedBinding binding = ActivityMapPointClickedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.intMapTitle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_selection_fragment);

        if(mapFragment != null) { mapFragment.getMapAsync(this); }

        clientHelper = new RetrofitClientHelper(this, this, "USER", null);
        dialogManager = new DialogManager(this);

        if(getIntent().hasExtra("metersRadius")) {
            metersRadius = IntentUtils.getIntIntent(getIntent(), "metersRadius");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        GoogleMapsUtils.defaultFocusMapOnItaly(googleMap);

        googleMap.setOnMapClickListener(this::mapClickListener);
    }

    private void mapClickListener(LatLng latLng) {
        GoogleMapsUtils.clearOldMarkers(gMap);
        updateMapWithMarker(latLng);
        adsByRadiusFilterWithCustomCoordinates(latLng, this);
    }

    private void updateMapWithMarker(LatLng latLng) { gMap.addMarker(new MarkerOptions().position(latLng).title("My point")); }

    private void adsByRadiusFilterWithCustomCoordinates(LatLng latLng, FragmentActivity activity) {
        if(metersRadius != 0) {
            clientHelper.initializeRetrofit((context, serviceAPI) -> {
                RetrofitGetListHelper.RetrofitGetListCallback<List<String>> callback = manageAddressCallback(context, activity);
                listHelper(serviceAPI).getListFromFilterRadius(latLng.longitude, latLng.latitude, metersRadius, callback);
            });
        }
    }

    private RetrofitGetListHelper listHelper(PropertyServiceAPI serviceAPI) { return new RetrofitGetListHelper(serviceAPI); }

    private RetrofitGetListHelper.RetrofitGetListCallback<List<String>> manageAddressCallback(Context context, FragmentActivity activity) {
        return new RetrofitGetListHelper.RetrofitGetListCallback<>() {
            @Override
            public void onSuccess(List<String> addresses) {
                for(String address : addresses) {
                    LocationHelper.getLocationFromAddress(dialogManager, context, gMap, address, activity, 10, false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                dialogManager.showErrorDialog(CLASS_TAG, e.getMessage());
            }
        };
    }
}