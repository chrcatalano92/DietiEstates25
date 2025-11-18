package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add.reg_prov_cities_prop_title_models.LoadRegionSpinner;
import com.demo.dietiestates25.databinding.ActivityManualOfferAddBinding;
import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.manage_spinner.ManagementSpinnerCheckStatusHelper;
import com.demo.dietiestates25.support_classes.manage_spinner.PopulatorSpinnerHelper;
import com.demo.dietiestates25.support_classes.keyboard_manager.SetupKeyboardEvent;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManualOfferAddActivity extends AppCompatActivity {
    private Spinner regionSpinner;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner titlePropertySpinner;
    private Spinner offerStateSpinner;
    private List<String> regionsList;
    private List<String> provincesList;
    private List<String> citiesList;
    private List<String> titlePropertiesList;
    private List<String> offerStateList;
    private DialogManager dialogManager;
    private InputLayoutHelper layoutHelper;
    private ManualOfferKeys keys;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManualOfferAddBinding binding = ActivityManualOfferAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.manual_offer_title);

        SetupKeyboardEvent keyboardEvent = new SetupKeyboardEvent(this);
        keyboardEvent.closeKeyboardWithActionDone(findViewById(R.id.noteEditTxt));

        dialogManager = new DialogManager(this);

        layoutHelper = new InputLayoutHelper(this, dialogManager);

        regionSpinner = findViewById(R.id.regionsSpinner);
        provinceSpinner = findViewById(R.id.provincesSpinner);
        citySpinner = findViewById(R.id.citiesSpinner);
        titlePropertySpinner = findViewById(R.id.titlePropertiesSpinner);

        offerStateSpinner = findViewById(R.id.statusSpinner);

        keys = new ManualOfferKeys();

        layoutHelper.registerInput(keys.getClientFNameMOffer(), findViewById(R.id.firstNameEditTxt));
        layoutHelper.registerInput(keys.getClientLNameMOffer(), findViewById(R.id.lastNameEditTxt));
        layoutHelper.registerInput(keys.getClientEmailMOffer(), findViewById(R.id.emailEditTxt));
        layoutHelper.registerInput(keys.getClientPhoneMOffer(), findViewById(R.id.phoneNumberEditTxt));
        layoutHelper.registerInput(keys.getClientCellMOffer(), findViewById(R.id.cellPhoneEditTxt));
        layoutHelper.registerInput(keys.getClientMOffer(), findViewById(R.id.offerEditTxt));
        layoutHelper.registerInput(keys.getClientNoteMOffer(), findViewById(R.id.noteEditTxt));

        layoutHelper.registerInput(keys.getRegionKeySpinner(), regionSpinner);
        layoutHelper.registerInput(keys.getProvinceKeySpinner(), provinceSpinner);
        layoutHelper.registerInput(keys.getCityKeySpinner(), citySpinner);
        layoutHelper.registerInput(keys.getTitlePropertyKeySpinner(), titlePropertySpinner);
        layoutHelper.registerInput(keys.getManualOfferStateSpinner(), offerStateSpinner);

        sendBtn = findViewById(R.id.sendBtn);

        regionsList = initializeList();
        regionsList.add(ResourceUtils.getXMLString(this, R.string.spinner1_item0));

        provincesList = initializeList();
        provincesList.add(ResourceUtils.getXMLString(this, R.string.spinner2_item0));

        citiesList = initializeList();
        citiesList.add(ResourceUtils.getXMLString(this, R.string.spinner3_item0));

        titlePropertiesList = initializeList();
        titlePropertiesList.add(ResourceUtils.getXMLString(this, R.string.spinner4_item0));

        offerStateList = initializeList();
        offerStateList.add(ResourceUtils.getXMLString(this, R.string.spinner_offer_state_item0));
        offerStateList.add(ResourceUtils.getXMLString(this, R.string.accepted));
        offerStateList.add(ResourceUtils.getXMLString(this, R.string.rejected));

        initializeRetrofit(this, this);
    }

    private void onRetrofitInitialized(Button sendBtn, PropertyServiceAPI apiService) {
        initializeSpinners(apiService, layoutHelper);

        sendBtn.setOnClickListener(view -> {
            ManualOfferSender manualOffer = new ManualOfferSender(apiService, layoutHelper, keys, dialogManager);
            manualOffer.manualOfferDataSender();
        });
    }

    private void initializeRetrofit(Context context, Activity activity) {
        new Thread(() -> {
            RetrofitClient.RetrofitResult result = RetrofitClient.getRetrofitInstance2(context, "REA");

            activity.runOnUiThread(() -> {
                if(result.isSuccess()) {
                    PropertyServiceAPI apiService = result.serviceAPI;
                    onRetrofitInitialized(sendBtn, apiService);
                }
                else {
                    dialogManager.showErrorDialog("Retrofit error", result.error);
                }
            });
        }).start();
    }

    private void initializeSpinners(PropertyServiceAPI apiService, InputLayoutHelper layoutHelper) {
        PopulatorSpinnerHelper regionHelper = spinnerInit(regionSpinner, regionsList);
        PopulatorSpinnerHelper provinceHelper = spinnerInit(provinceSpinner, provincesList);
        PopulatorSpinnerHelper cityHelper = spinnerInit(citySpinner, citiesList);
        PopulatorSpinnerHelper propTitleHelper = spinnerInit(titlePropertySpinner, titlePropertiesList);

        spinnerInit(offerStateSpinner, offerStateList);

        ManagementSpinnerCheckStatusHelper managementStatusHelper = new ManagementSpinnerCheckStatusHelper(provinceSpinner, citySpinner, titlePropertySpinner);

        managementStatusHelper.spinnerState(provinceSpinner,false);
        managementStatusHelper.spinnerState(citySpinner,false);
        managementStatusHelper.spinnerState(titlePropertySpinner,false);

        LoadRegionSpinner region = new LoadRegionSpinner(layoutHelper, regionHelper, provinceHelper, cityHelper, propTitleHelper, managementStatusHelper);
        region.selectedRegion(apiService);
    }

    private PopulatorSpinnerHelper spinnerInit(Spinner spinner, List<String> listItem) {
        return new PopulatorSpinnerHelper(this, spinner, listItem);
    }

    private List<String> initializeList() { return new ArrayList<>(); }
}