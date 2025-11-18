package com.demo.dietiestates25.dashboards.real_estate_agent_area;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.dietiestates25.MainActivity;
import com.demo.dietiestates25.R;

import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.manual_offer_add.ManualOfferAddActivity;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.OfferManagementActivity;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create.PropertyListingCreateStepOneActivity;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.TrackingActivity;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.utils.ToastUtils;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.view.ViewHelper;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

public class READashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        DialogManager dialogManager = new DialogManager(this);

        setContentView(R.layout.activity_rea_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton logoutBtn = findViewById(R.id.exitBtn);
        ImageButton profileBtn = findViewById(R.id.profileBtn);

        TextView profTypeTxt = findViewById(R.id.profileType);

        CardView listingC = findViewById(R.id.card1);
        CardView manageOffersC = findViewById(R.id.card2);
        CardView addOfferC = findViewById(R.id.card3);
        CardView tracking = findViewById(R.id.card4);

        profTypeTxt.setText(ResourceUtils.getXMLString(this, R.string.reserv_entries3));

        logoutBtn.setOnClickListener(view1 -> dialogManager.showQuestionDialog(
                getString(R.string.logout_confirmation_message),
                confirmed -> {
                    if (confirmed) {
                        performREALogout();
                    }
                }
        ));

        profileBtn.setOnClickListener(view -> dialogManager.showProfileDialog("Agent User", "agentname@dietiestates25.agent.it", this::performREALogout));

        ViewHelper.setupCardViewClickListener(listingC, manageOffersC, addOfferC, tracking, this::handlerMenuItemClick);
    }

    private void performREALogout() {
        sessionManager.clearSession();
        ActivityUtils.openActivity(this, MainActivity.class);
        finish();
        ToastUtils.showLogoutSuccessToast(this);
    }

    private void handlerMenuItemClick(String menuItem) {
        switch (menuItem) {
            case "c1":
                ActivityUtils.openActivity(this, PropertyListingCreateStepOneActivity.class);
                break;
            case "c2":
                ActivityUtils.openActivity(this, OfferManagementActivity.class);
                break;
            case "c3":
                ActivityUtils.openActivity(this, ManualOfferAddActivity.class);
                break;
            default:
                ActivityUtils.openActivity(this, TrackingActivity.class);
        }
    }
}