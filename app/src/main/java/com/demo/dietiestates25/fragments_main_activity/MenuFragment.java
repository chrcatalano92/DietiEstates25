package com.demo.dietiestates25.fragments_main_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.AdminDashboardActivity;
import com.demo.dietiestates25.dashboards.real_estate_agency_manager_area.REAMDashboardActivity;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.READashboardActivity;
import com.demo.dietiestates25.dashboards.user_area.UserDashboardFragment;
import com.demo.dietiestates25.data.model.Role;
import com.demo.dietiestates25.databinding.FragmentMenuBinding;
import com.demo.dietiestates25.features.auth.ui.SignUpUsrFragment;
import com.demo.dietiestates25.features.auth.ui.LoginUsrFragment;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModel;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModelFactory;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.FragmentUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MenuFragment extends Fragment {

    private static final String TAG = "menuFragment";
    private FragmentMenuBinding binding;
    private DialogManager dialogManager;
    private AuthViewModel viewModel;
    private SessionManager sessionManager;

    /* Nota sulla Deprecazione di GoogleSignInClient: Hai ragione, l'API GoogleSignInClient è contrassegnata come @Deprecated.
    Google sta spingendo verso la nuova API Credential Manager (androidx.credentials). Tuttavia, il tuo flusso attuale (che usa GoogleSignInClient per ottenere un idToken da passare a Firebase) è ancora completamente funzionante ed è lo standard per l'integrazione di Firebase Auth.
    Migrare alla nuova API è un refactoring molto complesso.
    Per ora, puoi ignorare il warning di deprecazione, sapendo che il tuo codice funziona come previsto. */
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureGoogleSignIn();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogManager = new DialogManager(requireContext());
        sessionManager = new SessionManager(requireContext());
        AuthViewModelFactory factory = new AuthViewModelFactory(requireActivity());
        viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        binding.headerLayout.title.setText(R.string.menu);

        // Controlla se l'utente ha già una "chiave della stanza" valida
        if (sessionManager.isLoggedIn()) {
            // Se è loggato, reindirizzalo subito alla sua Dashboard.
            navigateToDashboardByRole();
        } else {
            // Altrimenti, se non è loggato, prepara la UI per il login/registrazione.
            setupPublicMenu();
        }

        setupListeners();
        observeViewModel();
    }

    /**
     * Reindirizza l'utente alla Dashboard corretta in base al ruolo letto dal Token JWT.
     */
    private void navigateToDashboardByRole() {
        String roleApiName = sessionManager.getUserRole();
        Role userRole = Role.fromApiName(roleApiName);

        if (userRole == null) {
            // Se il Token è corrotto o non ha un ruolo, forza il logout per sicurezza.
            sessionManager.clearSession();
            setupPublicMenu(); // Mostre la UI di login
            return;
        }

        if (userRole == Role.USER) {
            FragmentUtils.replaceFragment(getParentFragmentManager(), new UserDashboardFragment());
        } else if (userRole.isProfessional()) {
            // Se il ruolo è professionale, usa uno switch per l'activity corretta
            switch (userRole) {
                case ADMIN:
                    startActivityForRole(AdminDashboardActivity.class);
                    break;
                case AGENCY_MANAGER:
                    startActivityForRole(REAMDashboardActivity.class);
                    break;
                case AGENT:
                    startActivityForRole(READashboardActivity.class);
                    break;
                default:
                    // Se il Ruolo non è riconosciuto, l'utente non può accedere
                    sessionManager.clearSession();
                    setupPublicMenu();
                    Toast.makeText(getContext(), "Ruolo non valido.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * Imposta la UI per un utente non autenticato.
     */
    private void setupPublicMenu() {
        binding.loginSection.setVisibility(View.VISIBLE);
        binding.headerLayout.title.setVisibility(View.GONE);
        setupListeners();
        observeViewModel();
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        if (getActivity() != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        }
        mAuth = FirebaseAuth.getInstance();

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            dialogManager.showErrorDialog(TAG, "Login con Google fallito.");
                        }
                    }
                });
    }

    private void setupListeners() {
        binding.usrLoginBtn.setOnClickListener( view1 -> FragmentUtils.replaceFragment(getParentFragmentManager(), new LoginUsrFragment()));

        binding.usrRegBtn.setOnClickListener( view1 -> FragmentUtils.replaceFragment(getParentFragmentManager(), new SignUpUsrFragment()));

        binding.googleUsrLoginBtn.setOnClickListener(v -> startSocialLogin("Google"));
        binding.facebookUsrLoginBtn.setOnClickListener(v -> startSocialLogin("Facebook"));
        binding.githubUsrLoginBtn.setOnClickListener(v -> startSocialLogin("Github"));
    }

    private void startSocialLogin(String provider) {
        // Implementare il flusso di Login con Firebase Authentication per il provider.
        // Per prima cosa, AVVIARE il flusso di Login dell'SDK di Firebase (es. GoogleSignInClient).
        // Seconda cosa, OTTENERE il Token ID da Firebase/Google.
        // Terza cosa, PASSARE il Token a un Metodo in AuthViewModel: viewModel.loginWithSocialToken((idToken).
        if ("Google".equals(provider)) {
            // AVVIA IL FLUSSO DI LOGIN
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        } else if ("Facebook".equals(provider)) {
            // Avvia qui il flusso di login dell'SDK di Facebook.
            // Otterrai un 'accessToken' di Facebook.
            // Lo passerai a un nuovo metodo 'firebaseAuthWithFacebook (accessToken)'.
            showNotImplementedDialog(provider);
        } else if ("GitHub".equals(provider)) {
            // Avvia qui il flusso di Login di GitHub.
            showNotImplementedDialog(provider);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        // Se mAuth è null, lo reinizializziamo per sicurezza
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {

                        // Autenticazione con Firebase riuscita. Ora prendiamo il token per il NOSTRO Backend.
                        mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(tokentask -> {
                                    if (tokentask.isSuccessful()) {
                                        String firebaseIdToken = tokentask.getResult().getToken();

                                        // Passa il Token finale al ViewModel
                                        viewModel.loginWithSocialToken(firebaseIdToken);
                                    } else {
                                        dialogManager.showErrorDialog(TAG, "Impossibile ottenere il Token Firebase.");
                                    }
                        });
                    }
        });
    }

    private void showNotImplementedDialog(String apiName) {
        String message = String.format(getString(R.string.not_implemented_login_api), apiName);
        dialogManager.showInfoDialog(message);
    }

    private void observeViewModel() {
        // Questo Observer gestirà il Risultato del Login
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), event -> {
            if (event.getContentIfNotHandled() != null) {
                Toast.makeText(getContext(), "Accesso effettuato con successo!", Toast.LENGTH_SHORT).show();
                sessionManager.saveAuthToken(event.peekContent().getToken());
                navigateToDashboardByRole(); // Naviga alla dashboard
            }
        });
    }

    /**
     * Metodo Helper per avviare un'Activity e pulire lo Stack.
     */
    private void startActivityForRole(Class<?> activityClass) {
        if (getActivity() == null) return;
        Intent intent = new Intent(getActivity(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}