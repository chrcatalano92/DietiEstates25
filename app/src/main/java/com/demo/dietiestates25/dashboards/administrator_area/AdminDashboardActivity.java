package com.demo.dietiestates25.dashboards.administrator_area;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.dietiestates25.MainActivity;
import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.default_admin_pwd_changer.DefaultAdminPwdChangerActivity;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.personal_data.PersonalDataAdminActivity;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.user_management.UserManagementActivity;
import com.demo.dietiestates25.dashboards.administrator_area.services.new_agency_manager.NewAgencyManagerActivity;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.ServerStateHelper;
import com.demo.dietiestates25.dashboards.administrator_area.services.new_support_account.NewAdministrativeAssistantActivity;
import com.demo.dietiestates25.dashboards.administrator_area.services.new_support_account.NewAnalystActivity;
import com.demo.dietiestates25.dashboards.administrator_area.services.new_support_account.NewDeveloperActivity;
import com.demo.dietiestates25.dashboards.administrator_area.services.new_support_account.NewTechnicalSupportActivity;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.support_classes.SlidePanelHelper;
import com.demo.dietiestates25.utils.ToastUtils;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.support_classes.view.ViewHelper;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

public class AdminDashboardActivity extends AppCompatActivity {
    private PropertyServiceAPI apiService;
    private SessionManager sessionManager;
    private DialogManager dialogManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        dialogManager = new DialogManager(this);
        apiService = RetrofitClient.getRetrofitInstance(this);

        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton logoutBtn = findViewById(R.id.exitBtn);
        ImageButton profileBtn = findViewById(R.id.profileBtn);

        TextView profTypeTxt = findViewById(R.id.profileType);

        final LinearLayout slidePanel = findViewById(R.id.slide_panel);
        ImageButton toggleBtn = findViewById(R.id.toggleBtn);

        CardView newAM = findViewById(R.id.card1);
        CardView changePwd = findViewById(R.id.card);
        CardView personalData = findViewById(R.id.card4);
        CardView manageUsers = findViewById(R.id.card5);
        CardView serverState = findViewById(R.id.cardServer);
        TextView supp1 = findViewById(R.id.support1);
        TextView supp2 = findViewById(R.id.support2);
        TextView supp3 = findViewById(R.id.support3);
        TextView supp4 = findViewById(R.id.support4);

        profTypeTxt.setText(ResourceUtils.getXMLString(this, R.string.reserv_entries1));

        logoutBtn.setOnClickListener(view1 -> dialogManager.showQuestionDialog(
                getString(R.string.logout_confirmation_message),
                confirmed -> {
                    if (confirmed) {
                        performAdminLogout(); // Esegui il Logout solo se Admin clicca SI.
                    }
                }
        ));

        profileBtn.setOnClickListener(view -> dialogManager.showProfileDialog("Admin User", "admin@dietiestates25.admin.it", this::performAdminLogout));

        SlidePanelHelper.slidePanelManager(slidePanel, toggleBtn, android.R.drawable.arrow_down_float, android.R.drawable.arrow_up_float);

        setupCardViewClickListener(newAM, manageUsers, changePwd, personalData, serverState);
        setupTextViewClickListener(supp1, supp2, supp3, supp4);

    }

    private void performAdminLogout() {
        sessionManager.clearSession();
        ActivityUtils.openActivity(this, MainActivity.class);
        finish();
        ToastUtils.showLogoutSuccessToast(this);
    }

    private void setupCardViewClickListener(CardView c1, CardView c2, CardView c3, CardView c4, CardView c5) {
        ViewHelper.cardViewOnClick(c1, "c1", this::handlerMenuItemClick);
        ViewHelper.cardViewOnClick(c2, "c2", this::handlerMenuItemClick);
        ViewHelper.cardViewOnClick(c3, "c3", this::handlerMenuItemClick);
        ViewHelper.cardViewOnClick(c4, "c4", this::handlerMenuItemClick);
        ViewHelper.cardViewOnClick(c5, "c5", this::handlerMenuItemClick);
    }

    private void setupTextViewClickListener(TextView txtV1, TextView txtV2, TextView txtV3, TextView txtV4) {
        ViewHelper.txtViewOnClick(txtV1, "txtV1", this::handlerMenuItemClick);
        ViewHelper.txtViewOnClick(txtV2, "txtV2", this::handlerMenuItemClick);
        ViewHelper.txtViewOnClick(txtV3, "txtV3", this::handlerMenuItemClick);
        ViewHelper.txtViewOnClick(txtV4, "txtV4", this::handlerMenuItemClick);
    }

    /**
     * Gestisce i click su tutte le CardView e TextView, reindirizzando
     * alla Activity corretta utilizzando la classe ActivityUtils.
     */
    private void handlerMenuItemClick(String menuItem) {
        switch (menuItem) {
            case "c1":
                ActivityUtils.openActivity(this, NewAgencyManagerActivity.class);
                break;
            case "c2":
                ActivityUtils.openActivity(this, UserManagementActivity.class);
                break;
            case "c3":
                ActivityUtils.openActivity(this, DefaultAdminPwdChangerActivity.class);
                break;
            case "c4":
                ActivityUtils.openActivity(this, PersonalDataAdminActivity.class);
                break;
            case "c5":
                ServerStateHelper stateHelper = new ServerStateHelper(apiService, this);
                stateHelper.testServerPingConnection();
                break;
            case "txtV1":
                ActivityUtils.openActivity(this, NewAdministrativeAssistantActivity.class);
                break;
            case "txtV2":
                ActivityUtils.openActivity(this, NewTechnicalSupportActivity.class);
                break;
            case "txtV3":
                ActivityUtils.openActivity(this, NewDeveloperActivity.class);
                break;
            default:
                ActivityUtils.openActivity(this, NewAnalystActivity.class);
        }
    }
}