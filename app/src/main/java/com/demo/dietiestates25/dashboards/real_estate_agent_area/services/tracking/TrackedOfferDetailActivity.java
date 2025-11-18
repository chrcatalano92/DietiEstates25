package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityTrackedOfferDetailBinding;
import com.demo.dietiestates25.support_classes.Utility;

import java.util.Objects;

public class TrackedOfferDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TRACKED_OFFER_ID = "tracked_offer_id_extra_detail";
    private ActivityTrackedOfferDetailBinding binding;
    private AgentTrackingViewModel trackingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackedOfferDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        trackingViewModel = new ViewModelProvider(this).get(AgentTrackingViewModel.class);

        String offerIdToLoad = getIntent().getStringExtra(EXTRA_TRACKED_OFFER_ID);

        if(offerIdToLoad != null && !offerIdToLoad.isEmpty()) {
            observeSpecificOfferDetails(offerIdToLoad);
        }
        else {
            Toast.makeText(this, R.string.id_offer_missing_or_invalid, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void observeSpecificOfferDetails(String offerId) {
        trackingViewModel.getSpecificTrackedOfferLiveData(offerId).observe(this, offerData -> {
            if(offerData != null) {
                populateUI(offerData);
            }
            else {
                Toast.makeText(this, getString(R.string.offer_details_not_found_for_id, offerId), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(TrackedOfferData offerData) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_tracked_offer_detail_formatted, offerData.getPropertyTitle()));

        binding.contentTrackedOfferDetail.detailClientName.setText(offerData.getClientNameSurname());
        binding.contentTrackedOfferDetail.detailClientId.setText(offerData.getClientId());
        binding.contentTrackedOfferDetail.detailPropertyTitle.setText(offerData.getPropertyTitle());
        binding.contentTrackedOfferDetail.labelDetailPropertyId.setText(offerData.getPropertyId());
        binding.contentTrackedOfferDetail.detailOfferPrice.setText(Utility.formatPrice(offerData.getOfferPrice()));
        binding.contentTrackedOfferDetail.detailActionDatetime.setText(offerData.getActionDateTime());

        String finalStatusText = offerData.getOffersStatus();
        int counterOfferAttempts = offerData.getCounterOfferAttempts();

        if(counterOfferAttempts > 0) {
            binding.contentTrackedOfferDetail.labelDetailCounterAttempts.setVisibility(View.VISIBLE);
            binding.contentTrackedOfferDetail.detailCounterofferAttempts.setVisibility(View.VISIBLE);
            binding.contentTrackedOfferDetail.detailCounterofferAttempts.setText(String.valueOf(counterOfferAttempts));
        }
        else {
            binding.contentTrackedOfferDetail.labelDetailCounterAttempts.setVisibility(View.GONE);
            binding.contentTrackedOfferDetail.detailCounterofferAttempts.setVisibility(View.GONE);
        }

        binding.contentTrackedOfferDetail.detailOfferStatus.setText(finalStatusText);

        setColorStatusActions(offerData);

        setClientNotes(offerData);
    }

    private void setColorStatusActions(TrackedOfferData offerData) {
        int statusColor;

        if(offerData.getOffersStatus().equalsIgnoreCase(getString(R.string.accepted))) {
            statusColor = ContextCompat.getColor(this, R.color.green_accepted);
        }
        else if(offerData.getOffersStatus().equalsIgnoreCase(getString(R.string.rejected))) {
            statusColor = ContextCompat.getColor(this, R.color.red_rejected);
        }
        else if(offerData.getOffersStatus().equalsIgnoreCase(getString(R.string.counteroffer_sent_status))) {
            statusColor = ContextCompat.getColor(this, R.color.blue_counteroffer);
        }
        else {
            statusColor = ContextCompat.getColor(this, R.color.Background);
        }

        binding.contentTrackedOfferDetail.detailOfferStatus.setTextColor(statusColor);
    }

    private void setClientNotes(TrackedOfferData offerData) {
        if(offerData.getClientNotes() != null && !offerData.getClientNotes().isEmpty()) {
            binding.contentTrackedOfferDetail.labelDetailClientName.setVisibility(View.VISIBLE);
            binding.contentTrackedOfferDetail.detailClientNotes.setVisibility(View.VISIBLE);
            binding.contentTrackedOfferDetail.detailClientNotes.setText(offerData.getClientNotes());
        }
        else {
            binding.contentTrackedOfferDetail.detailClientNotesLabel.setVisibility(View.GONE);
            binding.contentTrackedOfferDetail.detailClientNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
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