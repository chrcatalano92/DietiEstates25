package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add;

import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;

public class ManualOfferSender {
    private PropertyServiceAPI apiService;
    private final DialogManager dialogManager;
    private final String name;
    private final String surname;
    private final String email;
    private final Integer tel;
    private final Integer cell;
    private final String region;
    private final String province;
    private final String city;
    private final String titleProperty;
    private final double offer;
    private final String noteOffer;
    private final String offerState;
    public ManualOfferSender(PropertyServiceAPI apiService, InputLayoutHelper layoutHelper, ManualOfferKeys keys, DialogManager dialogManager) {
        this.apiService = apiService;
        this.dialogManager = dialogManager;

        this.name = layoutHelper.getStringRequired(keys.getClientFNameMOffer());
        this.surname = layoutHelper.getStringRequired(keys.getClientLNameMOffer());
        this.email = layoutHelper.getString(keys.getClientEmailMOffer());
        this.region = layoutHelper.getStringSpinner(keys.getRegionKeySpinner());
        this.province = layoutHelper.getStringSpinner(keys.getProvinceKeySpinner());
        this.city = layoutHelper.getStringSpinner(keys.getCityKeySpinner());
        this.titleProperty = layoutHelper.getStringSpinner(keys.getTitlePropertyKeySpinner());
        this.offerState = layoutHelper.getStringSpinner(keys.getManualOfferStateSpinner());
        this.tel = layoutHelper.getInteger(keys.getClientPhoneMOffer());
        this.cell = layoutHelper.getInteger(keys.getClientCellMOffer());
        this.offer = layoutHelper.getDouble(keys.getClientMOffer());
        this.noteOffer = layoutHelper.getString(keys.getClientNoteMOffer());
    }

    public void manualOfferDataSender() {
        dialogManager.showInfoDialog("Name: " + name + " Surname: " + surname + " Email: " + email + " Tel: " + tel + " Cell: " + cell + " Region: " + region + " Prov: " + province + " City: " + city + " TP: " + titleProperty + " Offer: " + offer + " MOfferState: " + offerState + " NoteO: " + noteOffer);
    }
}
