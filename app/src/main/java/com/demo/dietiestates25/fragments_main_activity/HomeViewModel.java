package com.demo.dietiestates25.fragments_main_activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.data.repository.PropertyRepository;
import com.demo.dietiestates25.support_classes.arch.Event;
import com.demo.dietiestates25.support_classes.real_estate_ads.RealEstateAd;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private final PropertyRepository propertyRepository;

    private final MutableLiveData<List<RealEstateAd>> properties = new MutableLiveData<>();
    public LiveData<List<RealEstateAd>> getProperties() { return properties; }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return isLoading; }

    private final MutableLiveData<Event<String>> error = new MutableLiveData<>();
    public LiveData<Event<String>> getError() { return error; }


    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.propertyRepository = new PropertyRepository(application.getApplicationContext());
        loadProperties(); // Carica la lista iniziale all'avvio del ViewModel
    }

    public void loadProperties() {
        isLoading.setValue(true);
        propertyRepository.getAllProperties().enqueue(createCallback());
    }

    public void searchProperties(String query) {
        isLoading.setValue(true);
        propertyRepository.searchProperties(query).enqueue(createCallback());
    }

    // Metodo Helper privato (DRY) per gestire le risposte delle chiamate API
    private Callback<List<RealEstateAd>> createCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<RealEstateAd>> call, @NonNull Response<List<RealEstateAd>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    properties.setValue(response.body());
                } else {
                    error.setValue(new Event<>("Errore nel caricamento: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RealEstateAd>> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                error.setValue(new Event<>("Errore di rete: " + t.getMessage()));
            }
        };
    }
}
