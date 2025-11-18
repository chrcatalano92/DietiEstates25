package com.demo.dietiestates25.dashboards.user_area.services.tracking_sent_offer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.dietiestates25.support_classes.Utility;
import com.demo.dietiestates25.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientTrackingOfferViewModel extends ViewModel {
    private final MutableLiveData<List<ClientSentOfferData>> offers = new MutableLiveData<>();
    public LiveData<List<ClientSentOfferData>> getOffers() { return offers; }
    private final List<ClientSentOfferData> mockList = new ArrayList<>();

    public void loadOffers(FormatUtils formatUtils) {
        newOffer("OFF1", "Villa con Vista mare", 25000, formatUtils.dateTimeFormatter("12/02/2025", "12:00"));
        newOffer("OFF2", "Appartamento in centro", 15000, formatUtils.dateTimeFormatter("22/05/2025", "11:30"));

        offers.setValue(mockList);
    }

    private void newOffer(String offerId, String propTitle, double myOffer, String offerStatus) {
        mockList.add(new ClientSentOfferData(offerId, null, propTitle, myOffer, offerStatus));
    }
}
