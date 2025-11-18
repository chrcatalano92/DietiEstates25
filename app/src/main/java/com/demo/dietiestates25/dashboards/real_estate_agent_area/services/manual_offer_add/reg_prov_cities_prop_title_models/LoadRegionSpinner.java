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

public class LoadRegionSpinner {
    private final InputLayoutHelper layoutHelper;
    private final Spinner regionSpinner;
    private final Spinner provinceSpinner;
    private final PopulatorSpinnerHelper regionHelper;
    private final PopulatorSpinnerHelper provinceHelper;
    private final PopulatorSpinnerHelper cityHelper;
    private final PopulatorSpinnerHelper propTitleHelper;
    private final ManagementSpinnerCheckStatusHelper managementStatusHelper;

    public LoadRegionSpinner(InputLayoutHelper layoutHelper, PopulatorSpinnerHelper regionHelper, PopulatorSpinnerHelper provinceHelper, PopulatorSpinnerHelper cityHelper, PopulatorSpinnerHelper propTitleHelper, ManagementSpinnerCheckStatusHelper managementStatusHelper) {
        this.layoutHelper = layoutHelper;

        ManualOfferKeys keySpinner = new ManualOfferKeys();
        this.regionSpinner = layoutHelper.getSpinnerId(keySpinner.getRegionKeySpinner());
        this.provinceSpinner = layoutHelper.getSpinnerId(keySpinner.getProvinceKeySpinner());

        this.regionHelper = regionHelper;
        this.provinceHelper = provinceHelper;
        this.cityHelper = cityHelper;
        this.propTitleHelper = propTitleHelper;
        this.managementStatusHelper = managementStatusHelper;
    }

    private void setupRegionSpinner(PropertyServiceAPI apiService) {
        RetrofitManualOfferDataHelper dataHelper = new RetrofitManualOfferDataHelper(apiService);
        dataHelper.getNewItemsFromTable("region", "region_name", regionHelper);
    }

    public void selectedRegion(PropertyServiceAPI apiService) {

        setupRegionSpinner(apiService);

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position > 0) {
                    String selectedRegion = regionHelper.getSelectedItemSpinner();

                    managementStatusHelper.spinnerState(provinceSpinner, true);
                    managementStatusHelper.resetSpinnerSelection(provinceSpinner);
                    managementStatusHelper.disableSpinnersFrom(3);

                    LoadProvinceSpinner province = new LoadProvinceSpinner(selectedRegion, layoutHelper, provinceHelper, cityHelper, propTitleHelper, managementStatusHelper);
                    province.selectedProvince(apiService);
                }
                else {
                    managementStatusHelper.disableSpinnersFrom(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                managementStatusHelper.disableSpinnersFrom(2);
            }
        });
    }
}
