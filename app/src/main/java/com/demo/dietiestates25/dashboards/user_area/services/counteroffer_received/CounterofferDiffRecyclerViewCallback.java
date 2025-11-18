package com.demo.dietiestates25.dashboards.user_area.services.counteroffer_received;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.support_classes.GenericDiffRecyclerViewCallback;

import java.util.Objects;

public class CounterofferDiffRecyclerViewCallback extends GenericDiffRecyclerViewCallback<ClientCounterofferData> {
    @Override
    public boolean areItemsTheSame(@NonNull ClientCounterofferData oldItem, @NonNull ClientCounterofferData newItem) {
        return Objects.equals(oldItem.getCounterofferId(), newItem.getCounterofferId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ClientCounterofferData oldItem, @NonNull ClientCounterofferData newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected void populateChangePayload(@NonNull Bundle diffBundle, @NonNull ClientCounterofferData oldItem, @NonNull ClientCounterofferData newItem) {

    }
}
