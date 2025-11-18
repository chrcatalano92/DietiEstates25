package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.default_admin_pwd_changer;

import android.content.Context;

import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;

public class AdminDefaultPwdUpdateSender {
    private final PropertyServiceAPI apiService;
    private final String newPwd;
    private final DialogManager dialogManager;

    public AdminDefaultPwdUpdateSender(PropertyServiceAPI apiService, String newPwd, Context context) {
        this.apiService = apiService;
        this.newPwd = newPwd;
        this.dialogManager = new DialogManager(context);
    }

    public void adminPwdUpt() {
        if(!newPwd.isEmpty()) {
            dialogManager.showInfoDialog("New pwd: " + newPwd);
        }
    }
}
