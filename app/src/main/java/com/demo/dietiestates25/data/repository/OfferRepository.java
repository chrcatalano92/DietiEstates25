package com.demo.dietiestates25.data.repository;

import android.content.Context;
import com.demo.dietiestates25.data.dto.offer.OfferCreationDTO;
import com.demo.dietiestates25.data.dto.offer.OfferResponseDTO;
import com.demo.dietiestates25.data.source_api_auth.OfferApiService;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;

import retrofit2.Call;

public class OfferRepository {

    private final OfferApiService offerApiService;

    public OfferRepository(Context context) {
        this.offerApiService = RetrofitClient.getInstance(context).create(OfferApiService.class);
    }

    public Call<OfferResponseDTO> createOffer(OfferCreationDTO dto) {
        return offerApiService.createOffer(dto);
    }
}
