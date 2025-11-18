package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.user_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.UserManagementCategorySelectionActivity;
import com.demo.dietiestates25.databinding.ActivityUserManagementBinding;
import com.demo.dietiestates25.support_classes.Utility;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class UserManagementActivity extends AppCompatActivity {
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserManagementBinding binding = ActivityUserManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        utility = new Utility(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(utility.getXMLString(R.string.usr_man));

        MaterialCardView cardManageManagers = findViewById(R.id.card_manage_managers);
        MaterialCardView cardManageAgents = findViewById(R.id.card_manage_agents);
        MaterialCardView cardManageClients = findViewById(R.id.card_manage_clients);

        UserManagementContentKey management = new UserManagementContentKey();

        materialCardViewOnClickManagement(cardManageManagers, management.getChoiceKey(), management.getChoiceOne());
        materialCardViewOnClickManagement(cardManageAgents, management.getChoiceKey(), management.getChoiceTwo());
        materialCardViewOnClickManagement(cardManageClients, management.getChoiceKey(), management.getChoiceThree());
    }

    private void materialCardViewOnClickManagement(MaterialCardView cardView, String key, String contentKey) {
        cardView.setOnClickListener(view -> utility.openActivityWithDataIntent(UserManagementCategorySelectionActivity.class, key, contentKey));
    }
}