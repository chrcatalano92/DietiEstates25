package com.demo.dietiestates25.dashboards.user_area.services.counteroffer_received;

import androidx.annotation.Nullable;

import java.util.Objects;

public class ClientCounterofferData {
    private final String counterofferId;
    private final String propertyImgUrl;
    private final String propertyTitle;
    private final double yourOfferAmount;
    private final double agentCounterofferAmount;

    public ClientCounterofferData(String counterofferId, String propertyImgUrl, String propertyTitle, double yourOfferAmount, double agentCounterofferAmount) {
        this.counterofferId = counterofferId;
        this.propertyImgUrl = propertyImgUrl;
        this.propertyTitle = propertyTitle;
        this.yourOfferAmount = yourOfferAmount;
        this.agentCounterofferAmount = agentCounterofferAmount;
    }

    public String getCounterofferId() { return counterofferId; }

    public String getPropertyImgUrl() { return propertyImgUrl; }

    public String getPropertyTitle() { return propertyTitle; }

    public double getYourOfferAmount() { return yourOfferAmount; }

    public double getAgentCounterofferAmount() { return agentCounterofferAmount; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        ClientCounterofferData that = (ClientCounterofferData) obj;

        return Objects.equals(counterofferId, that.counterofferId) &&
                Objects.equals(propertyImgUrl, that.propertyImgUrl) &&
                Objects.equals(propertyTitle, that.propertyTitle) &&
                Double.compare(yourOfferAmount, that.yourOfferAmount) == 0 &&
                Double.compare(agentCounterofferAmount, that.agentCounterofferAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterofferId, propertyImgUrl, propertyTitle, yourOfferAmount, agentCounterofferAmount);
    }
}