package com.demo.dietiestates25.dashboards.real_estate_agency_manager_area;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.dietiestates25.MainActivity;
import com.demo.dietiestates25.R;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.utils.ToastUtils;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.view.ViewHelper;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

public class REAMDashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        DialogManager dialogManager = new DialogManager(this);

        setContentView(R.layout.activity_ream_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton logoutBtn = findViewById(R.id.exitBtn);
        ImageButton profileBtn = findViewById(R.id.profileBtn);

        TextView profTypeTxt = findViewById(R.id.profileType);

        CardView newREA = findViewById(R.id.card1);
        CardView changePwd = findViewById(R.id.card);

        profTypeTxt.setText(ResourceUtils.getXMLString(this, R.string.reserv_entries2));

        logoutBtn.setOnClickListener(view1 -> dialogManager.showQuestionDialog(
                getString(R.string.logout_confirmation_message),
                confirmed -> {
                    if (confirmed) {
                        performREAMLogout();
                    }
                }
        ));

        profileBtn.setOnClickListener(view -> dialogManager.showProfileDialog("Agency Manager User", "nameagencymanager@dietiestates25.agencymanager.it", this::performREAMLogout));

        setupCardViewClickListener(newREA, changePwd);
    }

    private void performREAMLogout() {
        sessionManager.clearSession();
        ActivityUtils.openActivity(this, MainActivity.class);
        finish();
        ToastUtils.showLogoutSuccessToast(this);
    }

    private void setupCardViewClickListener(CardView c1, CardView c2) {
        ViewHelper.cardViewOnClick(c1, "c1", this::handlerMenuItemClick);
        ViewHelper.cardViewOnClick(c2, "c2", this::handlerMenuItemClick);
    }

    private void handlerMenuItemClick(String menuItem) {
        if (menuItem.equals("c1")) {
            Toast.makeText(this, "c1", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "c2", Toast.LENGTH_SHORT).show();
        }
    }
}