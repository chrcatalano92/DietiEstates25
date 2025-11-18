package com.demo.dietiestates25.dashboards.user_area.services.tracking_sent_offer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityTrackingSentOfferBinding;
import com.demo.dietiestates25.support_classes.Utility;

public class TrackingSentOfferActivity extends AppCompatActivity {
    private ActivityTrackingSentOfferBinding binding;
    private ClientTrackingOfferViewModel trackingOfferViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackingSentOfferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();

        setupRecyclerView();

        trackingOfferViewModel = new ViewModelProvider(this).get(ClientTrackingOfferViewModel.class);

        observeViewModel();

        Utility utility = new Utility(this);
        trackingOfferViewModel.loadOffers(utility);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.Close);
            getSupportActionBar().setTitle(R.string.sent_offers_title);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.contentTrackingSentOffer.recViewSentOffers;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SentOfferAdapter offerAdapter = new SentOfferAdapter();
        recyclerView.setAdapter(offerAdapter);
    }

    private void observeViewModel() {
        trackingOfferViewModel.getOffers().observe(this, clientSentOfferData -> {
            boolean isEmpty = clientSentOfferData == null || clientSentOfferData.isEmpty();

            binding.contentTrackingSentOffer.emptyViewSentOffers.emptyViewList.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.contentTrackingSentOffer.recViewSentOffers.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

            if(isEmpty) {
                binding.contentTrackingSentOffer.emptyViewSentOffers.msg.setText(R.string.no_offers_sent);
            }

            SentOfferAdapter adapter = (SentOfferAdapter) binding.contentTrackingSentOffer.recViewSentOffers.getAdapter();

            if(adapter != null) { adapter.submitList(clientSentOfferData); }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}