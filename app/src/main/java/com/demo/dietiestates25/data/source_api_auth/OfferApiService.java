package com.demo.dietiestates25.data.source_api_auth;

import com.demo.dietiestates25.data.dto.offer.OfferCreationDTO;
import com.demo.dietiestates25.data.dto.offer.OfferResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Questa interfaccia Retrofit.
 * La sua Sola Responsabilità (SRP) è definisce il CONTRATTO HTTP per comunicare con l'API Backend riguardo alle Offerte.
 * Esiste un metodo 'createOffer'. Questo metodo invierà un 'OfferCreationDTO' come corpo (@Body) della richiesta. Si aspetta di ricevere un 'OfferResponseDTO' come risposta.
 * La chiamata è di tipo 'Call', il che significa che è asincrona e verrà gestita da Retrofit.
 */
public interface OfferApiService {
    @POST("/api/v1/offer/create")
    Call<OfferResponseDTO> createOffer(@Body OfferCreationDTO offerDTO);
}