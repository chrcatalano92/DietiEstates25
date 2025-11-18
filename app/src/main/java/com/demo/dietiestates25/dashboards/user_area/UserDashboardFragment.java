package com.demo.dietiestates25.dashboards.user_area;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.user_area.services.counteroffer_received.TrackingCounterofferReceivedActivity;
import com.demo.dietiestates25.dashboards.user_area.services.tracking_sent_offer.TrackingSentOfferActivity;
import com.demo.dietiestates25.databinding.FragmentUserDashboardBinding;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.fragments_main_activity.HomeFragment;
import com.demo.dietiestates25.fragments_main_activity.MenuFragment;
import com.demo.dietiestates25.support_classes.BadgeHelper;
import com.demo.dietiestates25.utils.ToastUtils;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.view.ViewHelper;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.FragmentUtils;

public class UserDashboardFragment extends Fragment {

    private static final String TAG = "UserDashboard";

    private FragmentUserDashboardBinding binding;
    private UserDashboardViewModel viewModel;
    private SessionManager sessionManager;
    private DialogManager dialogManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sessionManager = new SessionManager(requireContext());
        dialogManager = new DialogManager(requireContext());
        viewModel = new ViewModelProvider(this).get(UserDashboardViewModel.class);

        setupUI();
        setupListeners();
        observeViewModel();
    }

    private void setupUI() {
        // Imposta i testi statici
        binding.addSettingsLayout.changePwd.setText(R.string.change_pwd);
        // Esegue logiche di UI complesse come l'aggiunta del badge.
        counterofferReceivedBadgeCounter(binding.card3);
    }

    private void setupListeners() {
        // Accede ai componenti tramite il binding e l'ID degli <include>
        binding.contentDashboard.exitBtn.setOnClickListener(v -> {
            Log.d(TAG, "Pulsante logout cliccato. Mostro dialogo di conferma.");
            dialogManager.showQuestionDialog(
                    getString(R.string.logout_confirmation_message),
                    confirmed -> {
                        if (confirmed) {
                            Log.i(TAG, "Logout confermato dall'utente.");
                            performUsrLogout();
                        } else {
                            Log.d(TAG, "Logout annullato dall'utente.");
                        }
                    }
            );
        });

        // Usa l'helper per le CardView, passando i riferimenti dal binding.
        ViewHelper.setupCardViewClickListener(binding.card1, binding.card2, binding.card3, binding.addSettingsLayout.card, this::handlerMenuItemClick);
    }

    private void observeViewModel() {
        // Osserva i Dati dal ViewModel e aggiorna la UI
        viewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            if (username != null) {
                Log.d(TAG, "Dati Utente caricati. Username: " + username);
                binding.contentDashboard.profileType.setText(username); // Niente più stringhe hardcodate.
            }
        });

        viewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                binding.contentDashboard.profileBtn.setOnClickListener(v -> {
                    Log.d(TAG, "Pulsante profilo cliccato. Apro il dialogo del profilo.");
                    dialogManager.showProfileDialog(viewModel.getUsername().getValue(), email, this::performUsrLogout);
                });
            }
        });
    }

    private void performUsrLogout() {
        Log.i(TAG, "Eseguo il Logout: Pulizia sessione e navigazione al MenuFragment.");
        sessionManager.clearSession();
        FragmentUtils.replaceFragment(getParentFragmentManager(), new MenuFragment());
        ToastUtils.showLogoutSuccessToast(requireContext());
    }

    private void handlerMenuItemClick(String menuItem) {
        Log.i(TAG, "CardView cliccato con ID: " + menuItem);
        switch (menuItem) {
            case "c1": // "Fai un'offerta su un immobile"
                Log.i(TAG, "Avvio la 'Modalità Offerta': Navigazione verso Home.");
                Toast.makeText(requireContext(), "Scegli un immobile dalla lista per fare un'offerta", Toast.LENGTH_LONG).show();
                // Crea una nuova istanza di HomeFragment e le passa lo stato "modalità offerta"
                HomeFragment homeFragmentForOffer = HomeFragment.newInstance(true);
                FragmentUtils.replaceFragment(getParentFragmentManager(), homeFragmentForOffer);
                break;
            case "c2":
                Log.d(TAG, "Navigo verso lo Storico delle Offerte Inviate.");
                ActivityUtils.openActivity(requireContext(), TrackingSentOfferActivity.class);
                break;
            case "c3":
                Log.d(TAG, "Navigo verso lo Storico delle Controproposte ricevute.");
                ActivityUtils.openActivity(requireContext(), TrackingCounterofferReceivedActivity.class);
                break;
            case "card":
                Log.d(TAG, "Navigo verso la schermata di Cambio Password.");
                // L'ID della CardView per il cambio password
                // Naviga alla schermata/dialogo per il cambio password.
                Toast.makeText(requireContext(), "Navigazione a Cambio Password", Toast.LENGTH_SHORT).show();
                break;
            default:
                // Non fa nulla o logga un avviso per il debug.
                Log.w(TAG, "Azione non riconosciuta per il Menu Item: " + menuItem);
                break;
        }
    }

    private void counterofferReceivedBadgeCounter(CardView cardView) {
        BadgeHelper badgeHelper = new BadgeHelper(requireContext());
        badgeHelper.addCustomBadgeToCardView(cardView, 12);
        badgeHelper.updateCustomBadge(cardView, 400);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null; // Previene memory leak
    }
}