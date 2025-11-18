package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.repository;

import androidx.annotation.Nullable;

import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.TrackedOfferData;

import java.util.List;

public interface TrackingRepository {
    interface LoadTrackedOffersCallback {
        void onSuccess(List<TrackedOfferData> offers);
        void onError(String message);
    }

    interface SimpleActionCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    void getAgentTrackedOffers(int page, int size, @Nullable String sortBy, boolean isAscending, LoadTrackedOffersCallback callback);

    void exportTrackedOffers(List<TrackedOfferData> offersToExport, SimpleActionCallback callback);

    void deleteTrackedOffers(List<String> offerIdToDelete, SimpleActionCallback callback);
}
