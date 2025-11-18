package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.OfferManagementActivity;

import java.util.List;

public class OfferListSetup {
    private OfferListSetup(){}

    public static void manageOffers(OfferManagementActivity managementActivity, RecyclerView recV, List<OfferData> offerDataList) {
        int clientImg = R.drawable.account_img_dark;
        int clientId = 33;
        String nameSurname = "Mario Rossi";
        int propId1 = 1;
        int propId2 = 2;
        int propId3 = 3;
        String typeProp = "In affitto";
        double offer = 336.66;
        String timestamp = "12/04/2025 - 12:00";
        String notes = "bla bla bla";
        String offerStatus = " - ";

        String[] strings = {nameSurname, typeProp, timestamp, notes, offerStatus};
        int[] values1 = {clientImg, clientId, propId1};
        int[] values2 = {clientImg, clientId, propId2};
        int[] values3 = {clientImg, clientId, propId3};

        addRealEstateOffers(offerDataList, strings, values1, offer);

        addRealEstateOffers(offerDataList, strings, values2, offer);

        addRealEstateOffers(offerDataList, strings, values3, offer);

        OfferAdapter adapter = new OfferAdapter(offerDataList, managementActivity);
        recV.setAdapter(adapter);
    }

    private static void addRealEstateOffers(List<OfferData> offersList, String[] strings, int[] values, double offer) {
        OfferData offerData = new OfferData(strings, values, offer);
        addOfferToList(offersList, offerData);
    }

    private static void addOfferToList(List<OfferData> offerDataList, OfferData offerData) {
        offerDataList.add(offerData);
    }
}
