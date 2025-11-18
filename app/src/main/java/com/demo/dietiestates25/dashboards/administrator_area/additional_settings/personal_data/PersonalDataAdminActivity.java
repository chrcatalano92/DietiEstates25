package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.personal_data;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.default_admin_pwd_changer.DefaultAdminPwdChangerActivity;
import com.demo.dietiestates25.databinding.ActivityPersonalDataAdminBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.profile_helper.ProfileViewHelper;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.time.LocalDateTime;
import java.util.Objects;

public class PersonalDataAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPersonalDataAdminBinding binding = ActivityPersonalDataAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        DialogManager dialogManager = new DialogManager(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(ResourceUtils.getXMLString(this, R.string.pers_data));

        ProfileViewHelper profileViewHelper = new ProfileViewHelper(binding.getRoot(), this, dialogManager, DefaultAdminPwdChangerActivity.class);

        profileViewHelper.setProfileImg(null);
        profileViewHelper.setEditTxtFullName("Mario Rossi");
        profileViewHelper.setEditTxtUsername("mario.rossi");
        profileViewHelper.setEditTxtEmail("mario.rossi@dietiestates25.it");
        profileViewHelper.setEditTxtPwd("Salamander2!@");
        profileViewHelper.setEditTxtCurrentRole("Administrator (Admin Role)");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 12, 0);
            profileViewHelper.setEditTextLastAccess(dateTime);
        }

        new OnProfileSaveChangesListenerImpl(profileViewHelper, dialogManager);
    }
}