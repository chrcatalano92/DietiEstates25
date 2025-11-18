package com.demo.dietiestates25.dashboards.administrator_area.services.new_support_account;

import android.os.Bundle;
import android.widget.TextView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityNewSupportAccountBinding;
import com.demo.dietiestates25.support_classes.Utility;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class NewAnalystActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNewSupportAccountBinding binding = ActivityNewSupportAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Utility utility = new Utility(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String an = utility.getXMLString(R.string.an);
        String title = utility.getXMLString(R.string.create_account_label) + " " + an;
        getSupportActionBar().setTitle(title);

        TextView newAn = findViewById(R.id.accountInfo);
        String info = utility.getXMLString(R.string.new_support_account_info) + "\n" + an;
        newAn.setText(info);
    }
}