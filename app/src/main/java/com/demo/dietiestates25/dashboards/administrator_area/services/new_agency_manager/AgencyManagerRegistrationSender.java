package com.demo.dietiestates25.dashboards.administrator_area.services.new_agency_manager;

import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;

public class AgencyManagerRegistrationSender {
    private final PropertyServiceAPI apiService;
    private final DialogManager dialogManager;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String defaultPwd;

    public AgencyManagerRegistrationSender(PropertyServiceAPI apiService, DialogManager dialogManager, InputLayoutHelper layoutHelper, NewAgencyManagerKeys keys) {
        this.apiService = apiService;
        this.dialogManager = dialogManager;

        this.firstName = layoutHelper.getString(keys.getAgencyMFName());
        this.lastName = layoutHelper.getString(keys.getAgencyMLName());
        this.email = layoutHelper.getString(keys.getAgencyMEmail());
        this.defaultPwd = layoutHelper.getString(keys.getAgencyMDefPwd());
    }

    public void registrationSender() {
        dialogManager.showInfoDialog("FName: " + firstName + " LName: " + lastName + " Email: " + email + " DefPwd: " + defaultPwd);
    }
}
