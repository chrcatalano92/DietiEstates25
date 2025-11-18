package com.demo.dietiestates25.features.auth.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.AdminDashboardActivity;
import com.demo.dietiestates25.dashboards.real_estate_agency_manager_area.REAMDashboardActivity;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.READashboardActivity;
import com.demo.dietiestates25.data.dto.AuthResponse;
import com.demo.dietiestates25.data.model.Role;
import com.demo.dietiestates25.databinding.FragmentProfessionalLoginBinding;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModel;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModelFactory;
import com.demo.dietiestates25.fragments_main_activity.ReservedAreaFragment;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.FragmentUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Fragment per la UI di Login per tutti i RUOLI PROFESSIONALI (ADMIN, AGENCY MANAGER, AGENTE).
 * Riceve il Ruolo come Argomento per Persoanlizzare la UI e la navigazione POST-LOGIN.
 */
public class LoginProfessionalFragment extends Fragment {

    private static final String ARG_ROLE = "user_role";
    private Role currentRole; // Il ruolo per cui stiamo effettuando il login

    private FragmentProfessionalLoginBinding binding;
    private AuthViewModel viewModel;
    private SessionManager sessionManager;
    private DialogManager dialogManager;


    /**
     * Factory method per creare una nuova istanza di questo Fragment.
     * Questo è il modo corretto e sicuro per passare dati a un Fragment.
     * @param role Il Ruolo per cui si sta creando la Schermata di Login.
     * @return Una nuova istanza di LoginProfessionalFragment
     */
    public static LoginProfessionalFragment newInstance(Role role) {
        LoginProfessionalFragment fragment = new LoginProfessionalFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recupera il Ruolo dagli argomenti prima che la View venga creata.
        if (getArguments() != null) {

            Serializable roleSerializable = getArguments().getSerializable(ARG_ROLE);

            if (roleSerializable instanceof Role) {
                currentRole = (Role) roleSerializable;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfessionalLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        dialogManager = new DialogManager(requireContext());
        AuthViewModelFactory factory = new AuthViewModelFactory(requireActivity());
        viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        setupUI();
        setupListeners();
        observeViewModel();
    }

    private void setupUI() {
        // Personalizza la UI in base al Ruolo ricevuto
        if (currentRole != null) {
            String roleName = currentRole.name().replace("_", " ");
            String title = getString(R.string.login_title_for_role, roleName);
            binding.headerLayout.title.setText(title);
        }
    }

    private void setupListeners() {
        binding.loginFromLayout.loginBtn.setOnClickListener(view -> {
            String identifier = Objects.requireNonNull(binding.loginFromLayout.emailUsername.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.loginFromLayout.pwd.getText()).toString();
            viewModel.onLoginClicked(identifier, password);
        });

        // Aggiungi un pulsante "Indietro"
        binding.imgBackBtn.setOnClickListener(view ->
                FragmentUtils.replaceFragment(getParentFragmentManager(), new ReservedAreaFragment())
        );
    }

    private void observeViewModel() {
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE);
            binding.loginFromLayout.loginBtn.setEnabled(!Boolean.TRUE.equals(isLoading));
        });

        viewModel.getLoginResult().observe(getViewLifecycleOwner(), event -> {
            AuthResponse response = event.getContentIfNotHandled();
            if (response != null && response.getToken() != null) {
                // LOGIN RIUSCITO!
                sessionManager.saveAuthToken(response.getToken());
                navigateToDashboardByRole();
            } else if (!event.hasBeenHandled()) {
                // LOGIN FALLITO
                dialogManager.showErrorDialog("Login Fallito", "Credenziali errate o errore di connessione. Riprova.");
            }
        });
    }

    /**
     * Reindirizza l'Utente alla Dashboard corretta in base al Ruolo letto dal Token JWT.
     * Usare il Ruolo del Token è più sicuro che usare 'currentRole'.
     */
    private void navigateToDashboardByRole() {
        String role = sessionManager.getUserRole();
        if (role == null) {
            dialogManager.showErrorDialog("Errore", "Ruolo utente non trovato nel token.");
            return;
        }

        Class<?> destinationActivity = null;

        switch (role) {
            case "ROLE_ADMIN":
                destinationActivity = AdminDashboardActivity.class;
                break;
            case "ROLE_AGENCY_MANAGER":
                destinationActivity = REAMDashboardActivity.class;
                break;
            case "ROLE_AGENT":
                destinationActivity = READashboardActivity.class;
                break;
            default:
                Toast.makeText(getContext(), "Ruolo non autorizzato per quest'area.", Toast.LENGTH_LONG).show();
                break;
        }

        if (destinationActivity != null) {
            // Queste flag puliscono lo Stack di Navigazione, impedendo all'Utente di Tornare
            // alla Schermata di Login con il tasto "Indietro".
            ActivityUtils.openActivityAndClearStack(requireContext(), destinationActivity);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
