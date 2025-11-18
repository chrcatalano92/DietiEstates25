package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add.reg_prov_cities_prop_title_models;

import com.demo.dietiestates25.support_classes.manage_spinner.PopulatorSpinnerHelper;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper.manual_offer_helper.RetrofitManualOfferDataHelper;

public class LoadPropertyTitleSpinner {
    private final String citySelected;
    private final PopulatorSpinnerHelper propTitleHelper;

    public LoadPropertyTitleSpinner(String citySelected, PopulatorSpinnerHelper propTitleHelper) {
        this.citySelected = citySelected;
        this.propTitleHelper = propTitleHelper;
    }

    public void setupPropertyTitle(PropertyServiceAPI apiService) {
        RetrofitManualOfferDataHelper dataHelper = new RetrofitManualOfferDataHelper(apiService);
        dataHelper.getNewItemsFromFilterTable("property_title", citySelected, propTitleHelper);
    }
}
