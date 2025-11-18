package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.user_management.UserManagementContentKey;
import com.demo.dietiestates25.databinding.ActivityUserManagementCategorySelectionBinding;
import com.demo.dietiestates25.support_classes.Utility;

import java.util.Objects;

public class UserManagementCategorySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserManagementCategorySelectionBinding binding = ActivityUserManagementCategorySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contentUserManagementCategorySelection.emptyViewUserManagementInclude.msg.setText(R.string.no_user_manage_msg);

        setSupportActionBar(binding.toolbar);

        Utility utility = new Utility(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(utility.getXMLString(R.string.usr_man));

        FabGroupUserManagementCategorySelectionSetup fabGroupUserManagement = new FabGroupUserManagementCategorySelectionSetup(binding.getRoot());
        fabGroupUserManagement.leftFabGroupListener();
        fabGroupUserManagement.rightFabGroupListener();

        String choiceCategory = getIntent().getStringExtra(UserManagementContentKey.CHOICE_KEY);

        Toast.makeText(this, "Choice: " + choiceCategory, Toast.LENGTH_SHORT).show();

        PopulateListUsersManage populateListUsersManage = new PopulateListUsersManage(this, binding.getRoot(), choiceCategory);
        populateListUsersManage.populationChoiceCategoryManage();
    }
}