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

public class LoadProvinceSpinner {
    private final String regionSelected;
    private final InputLayoutHelper layoutHelper;
    private final Spinner provinceSpinner;
    private final Spinner citySpinner;
    private final PopulatorSpinnerHelper provinceHelper;
    private final PopulatorSpinnerHelper cityHelper;
    private final PopulatorSpinnerHelper propTitleHelper;
    private final ManagementSpinnerCheckStatusHelper managementStatusHelper;

    public LoadProvinceSpinner(String selectedRegion, InputLayoutHelper layoutHelper, PopulatorSpinnerHelper provinceHelper, PopulatorSpinnerHelper cityHelper, PopulatorSpinnerHelper propTitleHelper, ManagementSpinnerCheckStatusHelper managementStatusHelper) {
        this.regionSelected = selectedRegion;
        this.layoutHelper = layoutHelper;

        ManualOfferKeys keySpinner = new ManualOfferKeys();
        this.provinceSpinner = layoutHelper.getSpinnerId(keySpinner.getProvinceKeySpinner());
        this.citySpinner = layoutHelper.getSpinnerId(keySpinner.getCityKeySpinner());

        this.provinceHelper = provinceHelper;
        this.cityHelper = cityHelper;
        this.propTitleHelper = propTitleHelper;
        this.managementStatusHelper = managementStatusHelper;
    }

    private void setupProvinceSpinner(PropertyServiceAPI apiService) {
        RetrofitManualOfferDataHelper dataHelper = new RetrofitManualOfferDataHelper(apiService);
        dataHelper.getNewItemsFromFilterTable("province", regionSelected, provinceHelper);
    }

    public void selectedProvince(PropertyServiceAPI apiService) {
        setupProvinceSpinner(apiService);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position > 0) {
                    String selectedProvince = provinceHelper.getSelectedItemSpinner();

                    managementStatusHelper.spinnerState(citySpinner, true);
                    managementStatusHelper.resetSpinnerSelection(citySpinner);
                    managementStatusHelper.disableSpinnersFrom(4);

                    LoadCitySpinner city = new LoadCitySpinner(selectedProvince, layoutHelper, cityHelper, propTitleHelper, managementStatusHelper);
                    city.selectionCity(apiService);
                }
                else {
                    managementStatusHelper.disableSpinnersFrom(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                managementStatusHelper.disableSpinnersFrom(3);
            }
        });
    }
}
