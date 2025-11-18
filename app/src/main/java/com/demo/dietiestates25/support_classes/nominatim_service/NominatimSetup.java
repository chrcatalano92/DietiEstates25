package com.demo.dietiestates25.support_classes.nominatim_service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.R;

import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.LocationHelper;
import com.demo.dietiestates25.utils.GoogleMapsUtils;

import com.demo.dietiestates25.utils.ResourceUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Classe Helper (non può essere istanziata) con una singola responsabilità principale:
 *  1. GEOCODIFICA: Connette all'API di Nominatim (OpenStreetMap) per convertire un indirizzo testuale (es. "Napoli") in coordinate GPS (LatLng).
 *  2. THROTTLING: Include una logica di Thread.sleep (retrofitRequestMonitoring) per rispettare la policy di Nominatim (massimo 1 richiesta al secondo).
 *  3. PLOTTING (Responsabilità Extra): Dopo aver ottenuto le coordinate, aggiunge un marker e/o muove la telecamera sulla GoogleMap che le viene passata.
 */
public class NominatimSetup {
    private static final String TAG = "NominatimSetup";
    private static long lastRequestTime = 0;
    private static final long MIN_TIME_BETWEEN_REQUESTS = 1000;

    private NominatimSetup(){}

    public static void geocodeAddressRetrofit(Context context, DialogManager dialogManager, GoogleMap gMap, String address, int zoom, boolean addMarkerAndMove, boolean addOnlyMarker) {
        retrofitRequestMonitoring(dialogManager);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        String userAgent = ResourceUtils.getXMLString(context, R.string.app_name);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new UserAgentInterceptor(userAgent))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        NominatimService service = retrofit.create(NominatimService.class);

        Call<List<NominatimResult>> call = service.search(address, "json", 1);

        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<NominatimResult>> call, @NonNull retrofit2.Response<List<NominatimResult>> response) {
                lastRequestTime = getCurrentTime();

                if (response.isSuccessful() && response.body() != null) {
                    List<NominatimResult> results = response.body();

                    if (!results.isEmpty()) {
                        NominatimResult result = results.get(0);

                        double lat = result.getLatitudeDouble();
                        double lon = result.getLongitudeDouble();

                        LatLng location = LocationHelper.getLocation(lat, lon);

                        BitmapDescriptor icon = GoogleMapsUtils.setIconMarker(R.drawable.PropertyPinMarker);

                        if(addMarkerAndMove) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                GoogleMapsUtils.addMarker(gMap, address, location, icon);
                                GoogleMapsUtils.moveCamera(gMap, location, zoom);
                            });
                        }
                        else if(addOnlyMarker) {
                            new Handler(Looper.getMainLooper()).post(() -> GoogleMapsUtils.addMarker(gMap, address, location, icon));
                        }
                        else {
                            new Handler(Looper.getMainLooper()).post(() -> GoogleMapsUtils.moveCamera(gMap, location, zoom));
                        }
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> dialogManager.showErrorDialog(TAG, R.string.show_map_error));
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> dialogManager.showErrorDialog(TAG, R.string.nominatim_API_error));
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<NominatimResult>> call, @NonNull Throwable t) {

                lastRequestTime = getCurrentTime();

                String errorPrefix = ResourceUtils.getXMLString(context, R.string.maps_network_error);
                String errorMessage = errorPrefix + t.getMessage();

                new Handler(Looper.getMainLooper()).post(() -> dialogManager.showErrorDialog(TAG, errorMessage));
            }
        });
    }

    private static void retrofitRequestMonitoring(DialogManager dialogManager) {
        long currentTime = getCurrentTime();
        long elapsedTime = currentTime - lastRequestTime;

        if(elapsedTime < MIN_TIME_BETWEEN_REQUESTS) {
            try {
                Thread.sleep(MIN_TIME_BETWEEN_REQUESTS - elapsedTime);
            }
            catch(InterruptedException ie) {
                String error = Objects.requireNonNull(ie.getMessage());
                Log.e(TAG, "Thread.sleep interrotto", ie);
                dialogManager.showErrorDialog(TAG, error);
                Thread.currentThread().interrupt();
            }
        }
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
