package com.demo.dietiestates25;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.databinding.ActivityFullDescPropBinding;
import com.demo.dietiestates25.support_classes.real_estate_ads.PropertyAdapterKeys;

import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.IntentUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.util.Objects;

public class FullDescPropActivity extends AppCompatActivity {
    private ActivityFullDescPropBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullDescPropBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        PropertyAdapterKeys keys = new PropertyAdapterKeys();

        Intent intent = getIntent();

        // Usa i metodi statici di IntentUtils
        getSupportActionBar().setTitle(IntentUtils.getStringIntent(intent, keys.getTitle()));

        // Usa IntentUtils e il nuovo ResourceUtils
        String subtitle = IntentUtils.getStringIntent(intent, keys.getNameCity()) + " - " +
                ResourceUtils.getXMLString(this, R.string.region) + " " + // Corretto
                IntentUtils.getStringIntent(intent, keys.getNameRegion());
        getSupportActionBar().setSubtitle(subtitle);

        TextView textView = findViewById(R.id.det_full_description);

        // Usa il metodo statico di IntentUtils
        textView.setText(IntentUtils.getStringIntent(intent, keys.getDescription()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityUtils.openActivity(this, PropertyActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}