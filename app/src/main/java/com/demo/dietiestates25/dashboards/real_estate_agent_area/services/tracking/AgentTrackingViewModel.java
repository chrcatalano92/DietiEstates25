package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.repository.TrackingRepository;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.repository.TrackingRepositoryImpl;
import com.demo.dietiestates25.support_classes.arch.Event;
import com.demo.dietiestates25.support_classes.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgentTrackingViewModel extends AndroidViewModel {
    private final TrackingRepository trackingRepository;
    private final MutableLiveData<List<TrackedOfferData>> trackedOffersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> actionFeedbackLiveData = new MutableLiveData<>();
    private String currentSortBy = "timestamp";
    private boolean currentIsAscending = false;
    private final Utility utility;

    public AgentTrackingViewModel(@NonNull Application application) {
        super(application);

        this.trackingRepository = new TrackingRepositoryImpl(application);
        this.utility = new Utility(application);
    }

    public LiveData<List<TrackedOfferData>> getTrackedOffersLiveData() {
        return trackedOffersLiveData;
    }

    public LiveData<Event<String>> getActionFeedbackLiveData() {
        return actionFeedbackLiveData;
    }

    public void clearActionFeedback() { actionFeedbackLiveData.setValue(null); }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public void loadTrackedOffers(@Nullable String sortBy, boolean isAscending) {
        this.currentSortBy = (sortBy == null || sortBy.isEmpty()) ? "timestamp" : sortBy;
        this.currentIsAscending = isAscending;
        isLoadingLiveData.setValue(true);
        actionFeedbackLiveData.setValue(null);

        int page = 1;
        int size = 50;

        trackingRepository.getAgentTrackedOffers(page, size, this.currentSortBy, this.currentIsAscending, new TrackingRepository.LoadTrackedOffersCallback() {
            @Override
            public void onSuccess(List<TrackedOfferData> offers) {
                trackedOffersLiveData.postValue(offers);
                isLoadingLiveData.postValue(false);
            }

            @Override
            public void onError(String message) {
                actionFeedbackLiveData.postValue(new Event<>(message));
                trackedOffersLiveData.postValue(new ArrayList<>());

                isLoadingLiveData.postValue(false);
            }
        });
    }

    public LiveData<TrackedOfferData> getSpecificTrackedOfferLiveData(String offerUniqueId) {
        MutableLiveData<TrackedOfferData> specificOfferLiveData = new MutableLiveData<>();
        List<TrackedOfferData> currentList = trackedOffersLiveData.getValue();
        boolean found = false;

        if(currentList != null) {
            for(TrackedOfferData offer : currentList) {
                if(offer.getOfferUniqueId().equals(offerUniqueId)) {
                    specificOfferLiveData.setValue(offer);
                    found = true;
                    break;
                }
            }
        }

        if(!found) {
            specificOfferLiveData.setValue(null);
        }

        return specificOfferLiveData;
    }

    public void saveSelectedOffer(TrackedOfferData offer) {
        isLoadingLiveData.setValue(true);
        actionFeedbackLiveData.setValue(null);
        trackingRepository.exportTrackedOffers(Collections.singletonList(offer), createSimpleActionCallback());
    }

    private TrackingRepository.SimpleActionCallback createSimpleActionCallback() {
        return new TrackingRepository.SimpleActionCallback() {
            @Override
            public void onSuccess(String message) {
                setActionFeedbackLiveData(utility.getXMLString(R.string.offer_saved_successfully));
                setIsLoadingLiveData();
            }

            @Override
            public void onError(String message) {
                setActionFeedbackLiveData(message);
                setIsLoadingLiveData();
            }
        };
    }

    public void deleteSelectedOffer(String offerUniqueId) {
        isLoadingLiveData.setValue(true);
        actionFeedbackLiveData.setValue(null);
        trackingRepository.deleteTrackedOffers(Collections.singletonList(offerUniqueId), createDeleteActionCallback());
    }

    private TrackingRepository.SimpleActionCallback createDeleteActionCallback() {
        return new TrackingRepository.SimpleActionCallback() {
            @Override
            public void onSuccess(String message) {
                setActionFeedbackLiveData(utility.getXMLString(R.string.offer_successfully_canceled));
                loadTrackedOffers(currentSortBy, currentIsAscending);
            }

            @Override
            public void onError(String message) {
                setActionFeedbackLiveData(message);
                setIsLoadingLiveData();
            }
        };
    }

    private void setActionFeedbackLiveData(String msg) {
        actionFeedbackLiveData.postValue(new Event<>(msg));
    }

    /**
     * Hide loading
     */
    private void setIsLoadingLiveData() {
        isLoadingLiveData.postValue(false);
    }
}
