package com.demo.dietiestates25.dashboards.user_area.services.tracking_sent_offer;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.support_classes.GenericDiffRecyclerViewCallback;

import java.util.Objects;

public class OfferDiffRecyclerViewCallback extends GenericDiffRecyclerViewCallback<ClientSentOfferData> {
    @Override
    public boolean areItemsTheSame(@NonNull ClientSentOfferData oldItem, @NonNull ClientSentOfferData newItem) {
        return Objects.equals(oldItem.getOfferId(), newItem.getOfferId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ClientSentOfferData oldItem, @NonNull ClientSentOfferData newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected void populateChangePayload(@NonNull Bundle diffBundle, @NonNull ClientSentOfferData oldItem, @NonNull ClientSentOfferData newItem) {

    }
}
