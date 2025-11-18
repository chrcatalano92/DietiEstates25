package com.demo.dietiestates25.dashboards.user_area.services.counteroffer_received;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityTrackingCounterofferReceivedBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;

public class TrackingCounterofferReceivedActivity extends AppCompatActivity implements CounterofferReceivedAdapter.OnRespondClickListener {

    private ActivityTrackingCounterofferReceivedBinding binding;
    private ClientTrackingViewModel trackingViewModel;
    private CounterofferReceivedAdapter counterofferReceivedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackingCounterofferReceivedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();

        setupRecyclerView();

        trackingViewModel = new ViewModelProvider(this).get(ClientTrackingViewModel.class);

        observeViewModel();

        trackingViewModel.loadCounteroffers();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.Close);
            getSupportActionBar().setTitle(R.string.counteroffer_received_title);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.contentCounterofferReceived.recViewCounteroffer;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        counterofferReceivedAdapter = new CounterofferReceivedAdapter();
        counterofferReceivedAdapter.setOnRespondClickListener(this);

        recyclerView.setAdapter(counterofferReceivedAdapter);
    }

    private void observeViewModel() {
        trackingViewModel.getCounteroffers().observe(this, clientCounterofferData -> {
            boolean isEmpty = clientCounterofferData == null || clientCounterofferData.isEmpty();

            binding.contentCounterofferReceived.emptyViewCounteroffers.emptyViewList.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.contentCounterofferReceived.recViewCounteroffer.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

            if(isEmpty) {
                binding.contentCounterofferReceived.emptyViewCounteroffers.msg.setText(R.string.no_counteroffers_received);
            }

            counterofferReceivedAdapter.submitList(clientCounterofferData);
        });
    }

    @Override
    public void onRespondClick(ClientCounterofferData counterofferData) { onCardActionClick(counterofferData); }

    private void onCardActionClick(ClientCounterofferData counterofferData) {
        DialogManager dialogManager = new DialogManager(this);
        dialogManager.showDialogClientRespondToCounteroffer(counterofferData.getAgentCounterofferAmount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}