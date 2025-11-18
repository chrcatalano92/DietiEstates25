package com.demo.dietiestates25.dashboards.user_area.services.counteroffer_received;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientTrackingViewModel extends ViewModel {
    private final MutableLiveData<List<ClientCounterofferData>> counteroffers = new MutableLiveData<>();
    public LiveData<List<ClientCounterofferData>> getCounteroffers() { return counteroffers; }
    private final List<ClientCounterofferData> mockList = new ArrayList<>();

    public void loadCounteroffers() {
        newCounteroffer("OFF001","Villa con Vista mare", 250000, 24000);
        newCounteroffer("OFF002","Appartamento in centro", 15000, 16000);

        counteroffers.setValue(mockList);
    }

    private void newCounteroffer(String counterofferId, String propTitle, double yourOffer, double agentOffer) {
        mockList.add(new ClientCounterofferData(counterofferId, null, propTitle, yourOffer, agentOffer));
    }
}
