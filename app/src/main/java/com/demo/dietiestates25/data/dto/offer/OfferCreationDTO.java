package com.demo.dietiestates25.data.dto.offer;

import com.google.gson.annotations.SerializedName;

/**
 * Classe DTO per INVIARE i dati di creazione di un'offerta al Backend.
 * GSON serializza questa classe in un JSON.
 * I campi sono 'final' per renderlo un oggetto immutabile (buona pratica).
 */
public class OfferCreationDTO {

    @SerializedName("userId")
    private final long userId;

    @SerializedName("propertyId")
    private final long propertyId;

    @SerializedName("offeredAmount")
    private final double offeredAmount;

    @SerializedName("clientNotes")
    private final String clientNotes;

    /**
     * Costruttore di default
     * @param userId           | L'ID dell'utente loggato (da SessionManager)
     * @param propertyId       | L'ID dell'immobile
     * @param offeredAmount    | L'importo offerto
     * @param clientNotes      | Le note del cliente
     */
    public OfferCreationDTO(long userId, long propertyId, double offeredAmount, String clientNotes) {
        this.userId = userId;
        this.propertyId = propertyId;
        this.offeredAmount = offeredAmount;
        this.clientNotes = clientNotes;
    }

    /* GETTER METHOD */

    /**
     * Aggiungere Getters è opzionale per GSON (che usa reflection),
     * ma è una buona pratica per i DTO e può soddisfare alcuni tool di analisi statica.
     */
    public long getUserId() {
        return userId;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public double getOfferedAmount() {
        return offeredAmount;
    }

    public String getClientNotes() {
        return clientNotes;
    }
}
