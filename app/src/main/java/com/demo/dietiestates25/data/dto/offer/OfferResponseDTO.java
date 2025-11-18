package com.demo.dietiestates25.data.dto.offer;

import com.google.gson.annotations.SerializedName;

/**
 * DTO (Data Transfer Object) per ricevere la risposta dal Backend
 * dopo la creazione di un'offert
 * Modella il CONTRATTO DI RISPOSTA JSON definito dal Server
 */
public class OfferResponseDTO {

    @SerializedName("id")
    private Long id;

    @SerializedName("propertyId")
    private Long propertyId;

    @SerializedName("offeredAmount")
    private Double offeredAmount;

    @SerializedName("offerStatus")
    private String offerStatus;

    @SerializedName("offerTimestamp")
    private String offerTimestamp;


    /* GETTER METHOD */
    public Long getId() {
        return id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public Double getOfferedAmount() {
        return offeredAmount;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public String getOfferTimestamp() {
        return offerTimestamp;
    }
}
