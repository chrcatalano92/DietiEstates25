package com.demo.dietiestates25.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.data.dto.offer.OfferCreationDTO;
import com.demo.dietiestates25.data.dto.offer.OfferResponseDTO;
import com.demo.dietiestates25.data.repository.OfferRepository;
import com.demo.dietiestates25.support_classes.arch.Event;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyViewModel extends AndroidViewModel {

    private final OfferRepository offerRepository;


    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return isLoading; }

    private final MutableLiveData<Event<Boolean>> offerResult = new MutableLiveData<>();
    public LiveData<Event<Boolean>> getOfferResult() { return offerResult; }

    public PropertyViewModel(@NonNull Application application) {
        super(application);
        this.offerRepository = new OfferRepository(application.getApplicationContext());
    }

    public void sendOffer(long userId, long propertyId, double amount, String notes) {
        isLoading.setValue(true);

        OfferCreationDTO offerDTO = new OfferCreationDTO(userId, propertyId, amount, notes);

        offerRepository.createOffer(offerDTO).enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<OfferResponseDTO> call, @NonNull Response<OfferResponseDTO> response) {
                isLoading.setValue(false);
                offerResult.setValue(new Event<>(response.isSuccessful()));
            }

            @Override
            public void onFailure(@NonNull Call<OfferResponseDTO> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                offerResult.setValue(new Event<>(false));
            }
        });

    }
}