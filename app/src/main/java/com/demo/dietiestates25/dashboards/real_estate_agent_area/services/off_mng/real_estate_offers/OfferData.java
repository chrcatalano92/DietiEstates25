package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers;

public class OfferData {
    private final int clientImg;
    private final int clientId;
    private final String nameSurname;
    private final int propertyId;
    private final String typeProperty;
    private final double offer;
    private final String timestamp;
    private final String notes;
    private String offerStatus;

    public OfferData(String[] strings, int[] values, double offer) {
        this.clientImg = values[0];
        this.clientId = values[1];
        this.propertyId = values[2];
        this.offer = offer;
        this.nameSurname = strings[0];
        this.typeProperty = strings[1];
        this.timestamp = strings[2];
        this.notes = strings[3];
        this.offerStatus = strings[4];
    }

    public int getClientImg() {
        return clientImg;
    }

    public int getClientId() {
        return clientId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public double getOffer() {
        return offer;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }
}
