package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.support_classes.GenericDiffRecyclerViewCallback;

import java.util.Objects;

public class TrackedOfferDiffRecyclerViewCallback extends GenericDiffRecyclerViewCallback<TrackedOfferData> {
    @Override
    public boolean areItemsTheSame(@NonNull TrackedOfferData oldItem, @NonNull TrackedOfferData newItem) {
        return Objects.equals(oldItem.getOfferUniqueId(), newItem.getOfferUniqueId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull TrackedOfferData oldItem, @NonNull TrackedOfferData newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected void populateChangePayload(@NonNull Bundle diffBundle, @NonNull TrackedOfferData oldItem, @NonNull TrackedOfferData newItem) {
        if(!Objects.equals(oldItem.getOffersStatus(), newItem.getOffersStatus())) {
            diffBundle.putString("PAYLOAD_STATUS_UPDATE", newItem.getOffersStatus());
        }
    }
}
