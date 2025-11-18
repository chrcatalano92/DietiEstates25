package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.default_admin_pwd_changer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityDefaultAdminPwdChangerBinding;
import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.RandomPwdGenerator;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.support_classes.validation.Validation;
import com.demo.dietiestates25.support_classes.validation.validation_default_pwd.ValidationTextWatcherDefaultPwdChange;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class DefaultAdminPwdChangerActivity extends AppCompatActivity {

    private ValidationTextWatcherDefaultPwdChange validationDefPwdChange;
    private static final String ADMIN_NEW_PWD_KEY = "adminNewPwdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDefaultAdminPwdChangerBinding binding = ActivityDefaultAdminPwdChangerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Utility utility = new Utility(this);

        PropertyServiceAPI apiService = RetrofitClient.getRetrofitInstance(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(utility.getXMLString(R.string.content_title_def_pwd_admin));

        TextInputEditText editTxtCurrPwd = findViewById(R.id.editTxt_curr_pwd);
        TextInputEditText editTxtNewPwd = findViewById(R.id.editTxt_new_pwd);
        TextInputEditText editTxtPwdRep = findViewById(R.id.editTxt_pwd_rep);

        ImageButton btnGenerateNewDefaultPwd = findViewById(R.id.btn_generate_new_def_pwd);

        Button btnChangePwd = findViewById(R.id.btn_confirm_pwd_change);

        TextView pwdLinearTxt = findViewById(R.id.pwd_linear_txt);
        LinearProgressIndicator pwdLinearBar = findViewById(R.id.pwd_linear_bar);

        InputLayoutHelper layoutHelper = new InputLayoutHelper(utility, new DialogManager(this));
        PwdCriteriaTxtKeys criteriaTxtKeys = new PwdCriteriaTxtKeys();

        layoutHelper.registerInput(ADMIN_NEW_PWD_KEY, editTxtPwdRep);

        layoutHelper.registerInput(criteriaTxtKeys.getCriteriaTxtLength(), findViewById(R.id.criteriaTxtLength));
        layoutHelper.registerInput(criteriaTxtKeys.getCriteriaTxtUppercase(), findViewById(R.id.criteriaTxtUppercase));
        layoutHelper.registerInput(criteriaTxtKeys.getCriteriaTxtLowercase(), findViewById(R.id.criteriaTxtLowercase));
        layoutHelper.registerInput(criteriaTxtKeys.getCriteriaTxtNumber(), findViewById(R.id.criteriaTxtNumber));
        layoutHelper.registerInput(criteriaTxtKeys.getCriteriaTxtSpecialChars(), findViewById(R.id.criteriaTxtSpecialChars));

        validationDefPwdChange = new ValidationTextWatcherDefaultPwdChange(this, editTxtNewPwd, pwdLinearBar, pwdLinearTxt, layoutHelper, criteriaTxtKeys);
        Validation.repPwdValidation(editTxtPwdRep, editTxtNewPwd, utility);

        utility.showToastHelpMsg(btnGenerateNewDefaultPwd);

        btnGenerateNewDefaultPwd.setOnClickListener(view -> {
            editTxtNewPwd.setText(RandomPwdGenerator.secureDefaultPwd());
            editTxtPwdRep.setText(null);
        });

        btnChangePwd.setOnClickListener(view -> {
            AdminDefaultPwdUpdateSender pwdUpdateSender = new AdminDefaultPwdUpdateSender(apiService, layoutHelper.getString(ADMIN_NEW_PWD_KEY), this);
            pwdUpdateSender.adminPwdUpt();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(validationDefPwdChange != null) {
            validationDefPwdChange.cleanup();
        }
    }
}