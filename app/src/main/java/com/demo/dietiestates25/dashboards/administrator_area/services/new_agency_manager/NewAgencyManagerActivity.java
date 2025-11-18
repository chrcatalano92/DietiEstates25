package com.demo.dietiestates25.dashboards.administrator_area.services.new_agency_manager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityNewAgencyManagerBinding;
import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.RandomPwdGenerator;
import com.demo.dietiestates25.support_classes.Utility;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.support_classes.validation.Validation;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class NewAgencyManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNewAgencyManagerBinding binding = ActivityNewAgencyManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utility utility = new Utility(this);
        DialogManager dialogManager = new DialogManager(this);

        PropertyServiceAPI apiService = RetrofitClient.getRetrofitInstance(this);

        InputLayoutHelper layoutHelper = new InputLayoutHelper(utility, dialogManager);

        NewAgencyManagerKeys keys = new NewAgencyManagerKeys();

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = utility.getXMLString(R.string.create_account_label) + " " + utility.getXMLString(R.string.content_title_new_a_m);
        getSupportActionBar().setTitle(title);

        TextInputEditText editTxtFN = findViewById(R.id.editTxt_first_name);
        TextInputEditText editTxtLN = findViewById(R.id.editTxt_last_name);
        TextInputEditText editTxtEmail = findViewById(R.id.editTxt_email);
        TextInputEditText editTxtPwd = findViewById(R.id.editTxt_pwd);

        layoutHelper.registerInput(keys.getAgencyMFName(), editTxtFN);
        layoutHelper.registerInput(keys.getAgencyMLName(), editTxtLN);
        layoutHelper.registerInput(keys.getAgencyMEmail(), editTxtEmail);
        layoutHelper.registerInput(keys.getAgencyMDefPwd(), editTxtPwd);

        ImageButton btnPwd = findViewById(R.id.btn_generate_pwd);

        Button btnCreateAcc = findViewById(R.id.btn_create_manager_account);

        Validation.generalValidation(editTxtFN, this);
        Validation.generalValidation(editTxtLN, this);
        Validation.emailValidation(editTxtEmail, this);
        Validation.pwdValidation(editTxtPwd, this);

        utility.showToastHelpMsg(btnPwd);

        btnPwd.setOnClickListener(view -> editTxtPwd.setText(RandomPwdGenerator.secureDefaultPwd()));

        btnCreateAcc.setOnClickListener(view -> {
            AgencyManagerRegistrationSender sender = new AgencyManagerRegistrationSender(apiService, dialogManager, layoutHelper, keys);
            sender.registrationSender();
        });
    }
}