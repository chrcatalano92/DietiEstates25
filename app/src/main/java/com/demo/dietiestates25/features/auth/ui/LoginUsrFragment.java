package com.demo.dietiestates25.features.auth.ui;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.dto.AuthResponse;
import com.demo.dietiestates25.databinding.FragmentUsrLoginBinding;
import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModel;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModelFactory;
import com.demo.dietiestates25.fragments_main_activity.HomeFragment;
import com.demo.dietiestates25.fragments_main_activity.MenuFragment;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.FragmentUtils;

import java.util.Objects;

public class LoginUsrFragment extends Fragment {

    private static final String TAG = "LoginUsrFragment";

    private FragmentUsrLoginBinding binding;
    private AuthViewModel viewModel;
    private DialogManager dialogManager;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsrLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        dialogManager = new DialogManager(requireContext());
        AuthViewModelFactory factory = new AuthViewModelFactory(requireActivity());
        viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

//        Utility.imgBtnChangeFragment(homeBtn, getParentFragmentManager(), new MenuFragment());

        // Imposta la UI e i listener
        setupUI();
        setupListeners();
        observeViewModel();
    }

    private void setupUI() {
        binding.headerLayout.title.setText(R.string.login_title);
        setupClickableRegisterText(binding.tVRegisterUsr);
    }

    private void setupListeners() {
        // Pulsante per Tornare al Menu Principale
        binding.exitBtn.setOnClickListener(v -> FragmentUtils.replaceFragment(getParentFragmentManager(), new MenuFragment()));

        // Pulsante per il Recupero Password
        binding.loginFromLayout.btnPwdRecovery.setOnClickListener(v -> dialogManager.showDialogPwdRecovery());

        binding.loginFromLayout.loginBtn.setOnClickListener(v -> {
            // Raccoglie i dati dai campi di input
            String identifier = Objects.requireNonNull(binding.loginFromLayout.emailUsername.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.loginFromLayout.pwd.getText()).toString();

            viewModel.onLoginClicked(identifier, password);
        });
    }

    /**
     * Osserva i LiveData del ViewModel per reagire ai Cambiamenti di Stato.
     * Questa è la parte reattiva dell'Architettura MVVM.
     */
    private void observeViewModel(){
        // Osserva lo stato di caricamento per Mostrare/Nascondere la ProgressBar
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE);
            binding.loginFromLayout.loginBtn.setEnabled(!Boolean.TRUE.equals(isLoading)); // Disabilita il pulsante durante il caricamento.
        });

        // Osserva il Risultato del Tentativo di Login
        /* Rimuovere la Gestione degli Errori del primo observer. Il primo observer ha la sola responsabilità del successo. Quindi, no 'getLoginError().observe'.*/
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), event -> {
            AuthResponse response = event.getContentIfNotHandled();
            if (response != null && response.getToken() != null) {

                // --- LOGIN RIUSCITO! ---
                Toast.makeText(getContext(), "Login effettuato con successo", Toast.LENGTH_SHORT).show();

                // 1. Salva il Token nella Sessione Sicura
                sessionManager.saveAuthToken(response.getToken());

                // 2. Naviga alla Dashboard Principale dell'Utente
                FragmentUtils.replaceFragment(getParentFragmentManager(), new HomeFragment());
            }
        });

        viewModel.getLoginError().observe(getViewLifecycleOwner(), event -> {
            String errorMessage = event.getContentIfNotHandled();
            if (errorMessage != null) {
                // LOGIN FALLITO
                // Il Fragment usa il suo strumento (DialogManager) per mostrare l'errore
                dialogManager.showErrorDialog(TAG, "Login fallito", errorMessage);
            }
        });
    }

    /**
     * Rende l'ultima parola di un TextView cliccabile per navigare al Fragment di registrazione.
     */
    private void setupClickableRegisterText(TextView textView) {
        String fullTxt = getString(R.string.regUsrBtn);
        String clickableWord = ' ' + getString(R.string.here);
        SpannableString spannableString = new SpannableString(fullTxt + clickableWord);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                FragmentUtils.replaceFragment(getParentFragmentManager(), new SignUpUsrFragment());
            }
        };

        int startIndex = fullTxt.lastIndexOf(clickableWord);
        if (startIndex != -1) {
            spannableString.setSpan(clickableSpan, startIndex, startIndex + clickableWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + clickableWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Previene memory leak
    }
}