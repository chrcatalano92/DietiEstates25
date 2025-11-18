package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import androidx.annotation.Nullable;

import java.util.Objects;

public class TrackedOfferData {
    private final boolean isManual;
    private final int counterOfferAttempts;
    private final double offerPrice;
    private final String clientImgUrl;
    private final String offerUniqueId;
    private final String clientId;
    private final String clientNameSurname;
    private final String propertyId;
    private final String propertyTitle;
    private final String actionDateTime;
    private final String offersStatus;
    private final String clientNotes;

    public TrackedOfferData(Builder builder) {
        this.isManual = builder.isManual;
        this.clientImgUrl = builder.clientImgUrl;
        this.counterOfferAttempts = builder.counterOfferAttempts;
        this.offerPrice = builder.offerPrice;
        this.offerUniqueId = builder.offerUniqueId;
        this.clientId = builder.clientId;
        this.clientNameSurname = builder.clientNameSurname;
        this.propertyId = builder.propertyId;
        this.propertyTitle = builder.propertyTitle;
        this.actionDateTime = builder.actionDateTime;
        this.offersStatus = builder.offersStatus;
        this.clientNotes = builder.clientNotes;
    }

    public boolean isManual() { return isManual; }

    public int getCounterOfferAttempts() { return counterOfferAttempts; }

    public double getOfferPrice() { return offerPrice; }

    public String getClientImgUrl() { return clientImgUrl; }

    public String getOfferUniqueId() { return offerUniqueId; }

    public String getClientId() {
        return clientId;
    }

    public String getClientNameSurname() {
        return clientNameSurname;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getPropertyTitle() { return propertyTitle; }

    public String getActionDateTime() {
        return actionDateTime;
    }

    public String getOffersStatus() {
        return offersStatus;
    }

    public String getClientNotes() { return clientNotes; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        TrackedOfferData that = (TrackedOfferData) obj;

        return isManual == that.isManual &&
                counterOfferAttempts == that.counterOfferAttempts &&
                Double.compare(offerPrice, that.offerPrice) == 0 &&
                Objects.equals(clientImgUrl, that.clientImgUrl) &&
                Objects.equals(offerUniqueId, that.offerUniqueId) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientNameSurname, that.clientNameSurname) &&
                Objects.equals(propertyId, that.propertyId) &&
                Objects.equals(propertyTitle, that.propertyTitle) &&
                Objects.equals(actionDateTime, that.actionDateTime) &&
                Objects.equals(offersStatus, that.offersStatus) &&
                Objects.equals(clientNotes, that.clientNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isManual, counterOfferAttempts, offerPrice, clientImgUrl, offerUniqueId, clientId, clientNameSurname, propertyId, propertyTitle, actionDateTime, offersStatus, clientNotes);
    }

    public static class Builder {
        private boolean isManual;
        private int counterOfferAttempts;
        private double offerPrice;
        private String clientImgUrl;
        private String offerUniqueId;
        private String clientId;
        private String clientNameSurname;
        private String propertyId;
        private String propertyTitle;
        private String actionDateTime;
        private String offersStatus;
        private String clientNotes;

        public Builder isManual(boolean val) {
            this.isManual = val;
            return this;
        }

        public Builder counterOfferAttempts(int counterOfferAttempts) {
            this.counterOfferAttempts = counterOfferAttempts;
            return this;
        }

        public Builder offerPrice(double offerPrice) {
            this.offerPrice = offerPrice;
            return this;
        }

        public Builder clientImgUrl(String clientImgUrl) {
            this.clientImgUrl = clientImgUrl;
            return this;
        }

        public Builder offerUniqueId(String offerUniqueId) {
            this.offerUniqueId = offerUniqueId;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientNameSurname(String clientNameSurname) {
            this.clientNameSurname = clientNameSurname;
            return this;
        }

        public Builder propertyId(String propertyId) {
            this.propertyId = propertyId;
            return this;
        }

        public Builder propertyTitle(String propertyTitle) {
            this.propertyTitle = propertyTitle;
            return this;
        }

        public Builder actionDateTime(String actionDateTime) {
            this.actionDateTime = actionDateTime;
            return this;
        }

        public Builder offersStatus(String offersStatus) {
            this.offersStatus = offersStatus;
            return this;
        }

        public Builder clientNotes(String clientNotes) {
            this.clientNotes = clientNotes;
            return this;
        }

        public TrackedOfferData build() { return new TrackedOfferData(this); }
    }
}
