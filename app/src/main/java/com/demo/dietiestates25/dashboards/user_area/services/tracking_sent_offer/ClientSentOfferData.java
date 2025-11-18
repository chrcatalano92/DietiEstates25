package com.demo.dietiestates25.dashboards.user_area.services.tracking_sent_offer;

import androidx.annotation.Nullable;

import java.util.Objects;

public class ClientSentOfferData {
    private final String offerId;
    public final String propertyImgUrl;
    public final String propertyTitle;
    private final double myOfferAmount;
    private final String offerStatus;

    public ClientSentOfferData(String offerId, String propertyImgUrl, String propertyTitle, double myOfferAmount, String offerStatus) {
        this.offerId = offerId;
        this.propertyImgUrl = propertyImgUrl;
        this.propertyTitle = propertyTitle;
        this.myOfferAmount = myOfferAmount;
        this.offerStatus = offerStatus;
    }

    public String getOfferId() { return offerId; }

    public String getPropertyImgUrl() { return propertyImgUrl; }

    public String getPropertyTitle() { return propertyTitle; }

    public double getMyOfferAmount() { return myOfferAmount; }

    public String getOfferStatus() { return offerStatus; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        ClientSentOfferData that = (ClientSentOfferData) obj;

        return Objects.equals(offerId, that.offerId) &&
                Objects.equals(propertyImgUrl, that.propertyImgUrl) &&
                Objects.equals(propertyTitle, that.propertyTitle) &&
                Double.compare(myOfferAmount, that.myOfferAmount) == 0 &&
                Objects.equals(offerStatus, that.offerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId, propertyImgUrl, propertyTitle, myOfferAmount, offerStatus);
    }
}
