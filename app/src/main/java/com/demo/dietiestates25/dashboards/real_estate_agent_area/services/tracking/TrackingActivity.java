package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.databinding.ActivityTrackingBinding;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.Utility;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class TrackingActivity extends AppCompatActivity implements TrackingFiltersBottomSheet.FilterListener, TrackedOfferAdapter.OnItemClickListener {
    private ActivityTrackingBinding binding;
    private AgentTrackingViewModel trackingViewModel;
    private TrackedOfferAdapter trackedOfferAdapter;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupToolbar();

        setupRecyclerView();

        setupFab();

        setupActionButtons();

        binding.contentTrackingLayout.emptyViewTrackedOffersInclude.msg.setText(R.string.no_tracked_offers_msg);

        utility = new Utility(this);

        trackingViewModel = new ViewModelProvider(this).get(AgentTrackingViewModel.class);

        observeViewModel();

        trackingViewModel.loadTrackedOffers("actionDateTime", false);
    }

    private void setupToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.tracking_title);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.contentTrackingLayout.recViewTrackingOffers;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        trackedOfferAdapter = new TrackedOfferAdapter(this);
        trackedOfferAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(trackedOfferAdapter);
    }

    private void setupFab() {
        TooltipCompat.setTooltipText(binding.fabFilterTracking, getString(R.string.filter_tooltip));

        binding.fabFilterTracking.setOnClickListener(view -> {
            TrackingFiltersBottomSheet filtersBottomSheet = TrackingFiltersBottomSheet.newInstance();
            filtersBottomSheet.show(getSupportFragmentManager(), TrackingFiltersBottomSheet.TAG);
        });
    }

    private void setupActionButtons() {
        binding.actionBarTrackingOptions.actionSaveTrackedContainer.setOnClickListener(view -> {
            TrackedOfferData selected = trackedOfferAdapter.getSelectedItem();

            if(selected != null) {
                trackingViewModel.saveSelectedOffer(selected);
            }
            else {
                Toast.makeText(this, R.string.select_offer_to_save, Toast.LENGTH_SHORT).show();
            }
        });

        binding.actionBarTrackingOptions.actionPrintTrackedContainer.setOnClickListener(view -> {
            TrackedOfferData selected = trackedOfferAdapter.getSelectedItem();

            if(selected != null) {
                Toast.makeText(this, getString(R.string.printing_offer) + " " + selected.getOfferUniqueId() + getString(R.string.to_be_implement_suffix), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, R.string.select_offer_to_print, Toast.LENGTH_SHORT).show();
            }
        });

        binding.actionBarTrackingOptions.actionDeleteTrackedContainer.setOnClickListener(view -> {
            TrackedOfferData selected = trackedOfferAdapter.getSelectedItem();

            if(selected != null) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.confirm_delete_offer_title)
                        .setMessage(getString(R.string.confirm_delete_offer_message, selected.getPropertyTitle()))
                        .setNegativeButton(R.string.btn_cancel_desc, null)
                        .setPositiveButton(R.string.action_delete, (dialog, which) -> trackingViewModel.deleteSelectedOffer(selected.getOfferUniqueId()))
                        .show();
            }
            else {
                Toast.makeText(this, R.string.select_offer_to_delete, Toast.LENGTH_SHORT).show();
            }
        });

        updateActionButtonsState(false);
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onTrackingFiltersApplied(String sortBy, boolean isAscending) {
        Toast.makeText(this, getString(R.string.filter_sort_by_label, sortBy, (isAscending ? getString(R.string.ascending_sort_indicator) : getString(R.string.descending_sort_indicator))), Toast.LENGTH_SHORT).show();
        trackedOfferAdapter.setSelectedPosition(RecyclerView.NO_POSITION);
        updateActionButtonsState(false);
        trackingViewModel.loadTrackedOffers(sortBy, isAscending);
    }

    @Override
    public void onItemTrackedClick(TrackedOfferData offerData, int position) {
        int currentSelected = trackedOfferAdapter.getSelectedPosition();

        if(currentSelected == position) {
            trackedOfferAdapter.setSelectedPosition(RecyclerView.NO_POSITION);
            updateActionButtonsState(false);
        }
        else  {
            trackedOfferAdapter.setSelectedPosition(position);
            updateActionButtonsState(true);
        }

        onCardActionClick(offerData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void observeViewModel() {
        trackingViewModel.getTrackedOffersLiveData().observe(this, offers -> {
            View emptyStateTextView = binding.contentTrackingLayout.emptyViewTrackedOffersInclude.emptyViewList;
            View searchBarLayout = binding.contentTrackingLayout.searchBarClientTrackingInclude.searchBarLayout;
            View actionBar = binding.actionBarTrackingOptions.getRoot();
            View fabFilter = binding.fabFilterTracking;
            RecyclerView recyclerView = binding.contentTrackingLayout.recViewTrackingOffers;

            boolean isListEmpty = (offers == null || offers.isEmpty());

            if(isListEmpty) {
                setVisibility(emptyStateTextView, View.VISIBLE);
                setVisibility(searchBarLayout, View.GONE);
                setVisibility(recyclerView, View.GONE);
                setVisibility(actionBar, View.GONE);
                setVisibility(fabFilter, View.GONE);

                if(offers == null) {
                    Toast.makeText(this, R.string.error_loading_offer_history, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                emptyStateTextView.setVisibility(View.GONE);
                searchBarLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                setVisibility(actionBar, View.VISIBLE);
                setVisibility(fabFilter, View.VISIBLE);

                trackedOfferAdapter.submitList(new ArrayList<>(offers));
            }

            trackedOfferAdapter.setSelectedPosition(RecyclerView.NO_POSITION);
            updateActionButtonsState(false);
        });

        trackingViewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            // progress bar
        });

        trackingViewModel.getActionFeedbackLiveData().observe(this, event -> {
            if(event != null) {
                String feedbackMessage = event.getContentIfNotHandled();

                if(feedbackMessage != null && !feedbackMessage.isEmpty()) {
                    Toast.makeText(this, feedbackMessage, Toast.LENGTH_SHORT).show();
                    trackingViewModel.clearActionFeedback();
                }
            }
        });
    }

    private void setVisibility(View view, int transition) {
        if(view != null) {
            view.setVisibility(transition);
        }
    }

    private void updateActionButtonsState(boolean enabled) {
        binding.actionBarTrackingOptions.actionSaveTrackedContainer.setEnabled(enabled);
        binding.actionBarTrackingOptions.actionPrintTrackedContainer.setEnabled(enabled);
        binding.actionBarTrackingOptions.actionDeleteTrackedContainer.setEnabled(enabled);

        float alpha = enabled ? 1.0f : 0.5f;

        binding.actionBarTrackingOptions.actionSaveTrackedContainer.setAlpha(alpha);
        binding.actionBarTrackingOptions.actionPrintTrackedContainer.setAlpha(alpha);
        binding.actionBarTrackingOptions.actionDeleteTrackedContainer.setAlpha(alpha);
    }

    private void onCardActionClick(TrackedOfferData offerData) {
        String question = utility.getXMLString(R.string.tracker_question) + "\n" + offerData.getClientNameSurname() + "?";

        new DialogManager(this).showQuestionDialog(question, confirmed -> {
            if(confirmed) {
                utility.openActivity(TrackedOfferDetailActivity.class);
            }
        });
    }
}