package com.demo.dietiestates25.fragments_main_activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.demo.dietiestates25.AdvancedSearchActivity;
import com.demo.dietiestates25.data.model.Role;
import com.demo.dietiestates25.databinding.FragmentHomeBinding;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.support_classes.ProgressBarHelper;

import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.real_estate_ads.PropertyAdapter;
import com.demo.dietiestates25.support_classes.real_estate_ads.RealEstateAd;
import com.demo.dietiestates25.support_classes.real_estate_ads.SearchViewHelper;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClientHelper;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.FeedbackUtils;
import com.demo.dietiestates25.utils.FragmentUtils;
import com.demo.dietiestates25.viewmodels.PropertyViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PropertyAdapter.OnPropertyInteractionListener {

    private static final String TAG = "HomeFragment";
    private static final String ARG_IS_OFFER_MODE = "is_offer_mode";
    private boolean isOfferMode = false; // Default: modalità normale

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private PropertyViewModel propertyViewModel;
    private SessionManager sessionManager;
    private PropertyAdapter adapter;


    /**
     * Factory method per creare il fragment, specificando se è in "modalità offerta".
     */
    public static HomeFragment newInstance(boolean isOfferMode) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_OFFER_MODE, isOfferMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isOfferMode = getArguments().getBoolean(ARG_IS_OFFER_MODE, false);
            Log.d(TAG, "Fragment creato in 'Modalità Offerta': " + isOfferMode);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        propertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        sessionManager = new SessionManager(requireContext());

        setupRecyclerView();
        setupSearchView();
        handleOfferModeUI();
        observeViewModel();

        binding.advancedSearch.setOnClickListener(v ->
                ActivityUtils.openActivity(requireContext(), AdvancedSearchActivity.class)
        );

        RecyclerView recView = binding.recView;
        SearchView searchV = binding.search;
        ImageButton advancedSearchImgBtn = binding.advancedSearch;
        ProgressBarHelper progressBarHelper = new ProgressBarHelper(requireActivity(), binding.progressCircular);

        FeedbackUtils.showToastHelpMsg(requireContext(), advancedSearchImgBtn);

        RetrofitClientHelper retrofitClientHelper = new RetrofitClientHelper(requireContext(), requireActivity(), Role.USER.getApiName(), progressBarHelper);

        progressBarHelper.showProgress();
        retrofitClientHelper.initializeRetrofit((context, serviceAPI) -> {

            SearchViewHelper searchViewHelper = new SearchViewHelper(
                    serviceAPI,
                    retrofitClientHelper,
                    recView,
                    searchV,
                    progressBarHelper,
                    requireContext(),
                    new ArrayList<>()
            );

            searchViewHelper.loadRealEstateAds(serviceAPI);
        });
    }

    private void setupRecyclerView() {
        Log.d(TAG, "Setup dello RecyclerView in 'Modalità Offerta': " + isOfferMode);
        binding.recView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PropertyAdapter(new ArrayList<>(), requireContext(), isOfferMode, this);
        binding.recView.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "Eseguo la ricerca per: " + query);
                // Delega la logica di ricerca al ViewModel
                homeViewModel.searchProperties(query);
                binding.search.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    Log.d(TAG, "La ricerca è vuota, ricarico tutti gli annunci.");
                    // Ricarica la Lista Completa se la Ricerca è vuota
                    homeViewModel.loadProperties();
                }
                return false;
            }
        });
    }

    private void handleOfferModeUI() {
        if (isOfferMode) {
            // Mostra il messaggio temporaneo e il pulsante per Disattivare la kodalità Offerta.
            Snackbar.make(binding.getRoot(), "Sei in Modalità Offerta: clicca un annuncio per procedere.", BaseTransientBottomBar.LENGTH_LONG).show();
            binding.deactivatedOfferModeButton.setVisibility(View.VISIBLE);
            binding.deactivatedOfferModeButton.setOnClickListener(v -> {
                Log.d(TAG, "Disattivazione della Modalità Offerta.");
                // Ricarica il Fragment in Modalità Normale
                FragmentUtils.replaceFragment(getParentFragmentManager(), HomeFragment.newInstance(false));
            });
        }
    }

    private void observeViewModel() {
        homeViewModel.getProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                Log.d(TAG, "Ricevuti " + properties.size() + "annunci dal ViewModel. Aggiorno l'adapter.");
                adapter.setList(properties);
                binding.emptyView.setVisibility(properties.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        homeViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> binding.progressCircular.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE));

        homeViewModel.getError().observe(getViewLifecycleOwner(), event -> {
            String error = event.getContentIfNotHandled();
            if (error != null) {
                new DialogManager(requireContext()).showErrorDialog(TAG, error);
            }
        });

        propertyViewModel.getOfferResult().observe(getViewLifecycleOwner(), event -> {
            Boolean success = event.getContentIfNotHandled();
            if (success != null) {
                if (success) {
                    Toast.makeText(getContext(), "Offerta inviata con successo!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Errore durante l'invio dell'offerta.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDirectOfferClick(RealEstateAd property) {
        Log.i(TAG, "Click diretto per offerta sull'immobile: " + property.getTitle());
        new DialogManager(requireContext()).showDialogClientOffer(property.getTitle(), property.getIdProperty(), true,
                (propertyId, amount, notes) -> {
                    Log.d(TAG, "Dialogo confermato. Chiamo il ViewModel per inviare l'offerta.");

                    long userId = sessionManager.getUserId();
                    if (userId == -1) {
                        Toast.makeText(getContext(), "Errore: utente non valido.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    propertyViewModel.sendOffer(userId, propertyId, amount, notes);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}