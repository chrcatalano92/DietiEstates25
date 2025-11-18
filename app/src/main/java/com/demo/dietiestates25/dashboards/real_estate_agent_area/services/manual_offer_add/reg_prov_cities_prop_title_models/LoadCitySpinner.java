package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add.reg_prov_cities_prop_title_models;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.manage_spinner.ManagementSpinnerCheckStatusHelper;
import com.demo.dietiestates25.support_classes.manage_spinner.PopulatorSpinnerHelper;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add.ManualOfferKeys;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper.manual_offer_helper.RetrofitManualOfferDataHelper;

public class LoadCitySpinner {
    private final String provinceSelected;
    private final Spinner citySpinner;
    private final Spinner titlePropertySpinner;
    private final PopulatorSpinnerHelper cityHelper;
    private final PopulatorSpinnerHelper propTitleHelper;
    private final ManagementSpinnerCheckStatusHelper managementStatusHelper;

    public LoadCitySpinner(String provinceSelected, InputLayoutHelper layoutHelper, PopulatorSpinnerHelper cityHelper, PopulatorSpinnerHelper propTitleHelper, ManagementSpinnerCheckStatusHelper managementStatusHelper) {
        this.provinceSelected = provinceSelected;

        ManualOfferKeys keySpinner = new ManualOfferKeys();
        this.citySpinner = layoutHelper.getSpinnerId(keySpinner.getCityKeySpinner());
        this.titlePropertySpinner = layoutHelper.getSpinnerId(keySpinner.getTitlePropertyKeySpinner());

        this.cityHelper = cityHelper;
        this.propTitleHelper = propTitleHelper;
        this.managementStatusHelper = managementStatusHelper;
    }

    private void setupCitySpinner(PropertyServiceAPI apiService) {
        RetrofitManualOfferDataHelper dataHelper = new RetrofitManualOfferDataHelper(apiService);
        dataHelper.getNewItemsFromFilterTable("city", provinceSelected, cityHelper);
    }

    public void selectionCity(PropertyServiceAPI apiService) {
        setupCitySpinner(apiService);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position > 0) {
                    String selectedCity = cityHelper.getSelectedItemSpinner();

                    managementStatusHelper.spinnerState(titlePropertySpinner, true);

                    LoadPropertyTitleSpinner propertyTitle = new LoadPropertyTitleSpinner(selectedCity, propTitleHelper);
                    propertyTitle.setupPropertyTitle(apiService);
                }
                else {
                    managementStatusHelper.spinnerState(titlePropertySpinner, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                managementStatusHelper.spinnerState(titlePropertySpinner, false);
            }
        });
    }
}
