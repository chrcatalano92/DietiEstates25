package com.demo.dietiestates25.features.auth.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.dto.AuthResponse;
import com.demo.dietiestates25.data.dto.CitySuggestion;
import com.demo.dietiestates25.data.dto.LoginRequestDTO;
import com.demo.dietiestates25.data.dto.SignUpRequestDTO;
import com.demo.dietiestates25.features.auth.data_repository.AuthRepository;
import com.demo.dietiestates25.support_classes.arch.Event;
import com.demo.dietiestates25.support_classes.validation.Validation;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ViewModel (MVVM) per la logica di Autenticazione.
 * La sua SOLA RESPONSABILITÀ (SRP) è orchestrare la logica di business
 * (validazione, chiamate di rete) per lo Stato della UI di registrazione/login.
 */
public class AuthViewModel extends ViewModel {

    /* DIPENDENZE E STATO */
    private final AuthRepository authRepository;



    // --- Stato della UI (per il Data Binding) ---
    /* Se AuthViewModel non deve più gestire Stringhe, ma solo ID di risorsa, allora si mette 'Integer' */
    public final MutableLiveData<String> firstName = new MutableLiveData<>("");
    public final MutableLiveData<String> lastName = new MutableLiveData<>("");
    public final MutableLiveData<String> username = new MutableLiveData<>("");
    public final MutableLiveData<String> email = new MutableLiveData<>("");
    public final MutableLiveData<String> password = new MutableLiveData<>("");
    public final MutableLiveData<String> retypePassword = new MutableLiveData<>("");
    public final MutableLiveData<LocalDate> dateOfBirth = new MutableLiveData<>();
    public final MutableLiveData<String> placeOfBirth = new MutableLiveData<>("");


    /* Stato degli Eventi (per i dialoghi e la navigazione) */
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Event<Integer>> registrationResult = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> registrationError = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> validationError = new MutableLiveData<>();
    private final MutableLiveData<Event<AuthResponse>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> loginError = new MutableLiveData<>();


    /* LOGICA DATA DI NASCITA (18-90) */
    /* Esposti per il Fragment, calcolati una sola volta (DRY). */
    public final LocalDate maxDateAllowed;
    public final LocalDate minDateAllowed;
    public final long maxDateMillis;
    public final long minDateMillis;


    /* LOGICA AUTOCOMPLETE (Debouncer Java) */
    private final MutableLiveData<List<String>> citySuggestionsLiveData = new MutableLiveData<>();
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    /**
     * COSTRUTTORE
     */
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;

        // Inizializza i limiti di data (18-90 anni)
        LocalDate today = LocalDate.now();
        maxDateAllowed = today.minusYears(18);
        minDateAllowed = today.minusYears(90); // Limite (1935 -> ~90 anni)

        // Converti in Millisecondi UTC per il DatePickerDialog
        maxDateMillis = maxDateAllowed.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        minDateMillis = minDateAllowed.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    // ==================================================================
    // --- GETTERS PUBBLICI (per LiveData osservabili dal Fragment) ---
    // ==================================================================

    public LiveData<Boolean> isLoading() { return isLoading; }
    public LiveData<Event<Integer>> getRegistrationResult() { return registrationResult; }
    public LiveData<Event<String>> getRegistrationError() { return registrationError; }
    public LiveData<Event<Integer>> getValidationError() { return validationError; }
    public LiveData<Event<AuthResponse>> getLoginResult() { return loginResult; }
    public LiveData<Event<String>> getLoginError() { return loginError; }



    // ==================================================================
    // --- METODI PUBBLICI PRINCIPALI (API del ViewModel) ---
    // ==================================================================



    /**
     * Chiamato dal Fragment (View) quando l'utente clicca "Registrati".
     * Non riceve parametri perché legge i dati dal *proprio* stato (i LiveData).
     * Questa è la vera architettura MVVM (Basso Accoppiamento).
     */
    public void onRegisterClicked() {

        // 1. Esegue la VALIDAZIONE (chiama il metodo logico)
        Integer validationErrorId = findValidationErrorId();

        // 2. Gestisce l'ERRORE (responsabilità di UI)
        if (validationErrorId != null) {
            validationError.setValue(new Event<>(validationErrorId));
            return; // Interrompe l'esecuzione
        }

        isLoading.setValue(true);

        // Estrae i dati solo al momento della creazione del DTO
        SignUpRequestDTO request = new SignUpRequestDTO(
                firstName.getValue(),
                lastName.getValue(),
                username.getValue(),
                email.getValue(),
                password.getValue(),
                dateOfBirth.getValue(),
                placeOfBirth.getValue()
        );

        // 4. Delega la chiamata di rete al Repository (SRP)
        authRepository.registerUser(request, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                isLoading.setValue(false);
                registrationResult.setValue(new Event<>(R.string.registration_success));
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
                registrationError.setValue(new Event<>(message));
            }
        });
    }

    /**
     * Chiamato dal Fragment (View) quando l'utente clicca "Login".
     */
    public void onLoginClicked(String identifier, String password) {
        // Validazione base
        if (isEmpty(identifier) || isEmpty(password)) {
            loginError.setValue(new Event<>("Email/Username e password non possono essere vuoti."));
            return;
        }

        isLoading.setValue(true);
        /* Il DTO di LoginRequest */
        LoginRequestDTO request = new LoginRequestDTO(identifier, password);

        authRepository.loginUser(request, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                isLoading.setValue(false);
                loginResult.setValue(new Event<>(response)); // Invia l'intera risposta con il token
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
                loginError.setValue(new Event<>(message));
            }
        });
    }

    /**
     * Chiamato dal Fragment per il Login Social.
     */
    public void loginWithSocialToken(String idToken) {
        isLoading.setValue(true);
        authRepository.loginWithSocialToken(idToken, new AuthRepository.AuthCallback() {

            @Override
            public void onSuccess(AuthResponse response) {
                isLoading.setValue(false);
                loginResult.setValue(new Event<>(response));
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
                loginError.setValue(new Event<>(message));
            }
        });
    }

    /**
     * Chiamato dal Fragment (TextWatcher) ogni volta che l'utente digita nel campo "Luogo di Nascita".
     * Implementa il DEBOUNCING in puro Java.
     */
    public void onCityQueryChanged(String query) {
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }

        if (query.length() <= 1) {
            citySuggestionsLiveData.setValue(new ArrayList<>());
            return;
        }

        searchRunnable = () -> {
            /* Assicurati che il Repository abbia il metodo 'searchCities' */
            authRepository.searchCities(query, new AuthRepository.CitySearchCallback() {
                @Override
                public void onSuccess(List<CitySuggestion> suggestions) {
                    List<String> formatted = suggestions.stream()
                            .map(s -> s.getName() + " (" + s.getProvince() + ")")
                            .collect(Collectors.toList());
                    citySuggestionsLiveData.setValue(formatted);
                }

                @Override
                public void onError(String message) {
                    citySuggestionsLiveData.setValue(new ArrayList<>());
                }
            });
        };
        searchHandler.postDelayed(searchRunnable, 300);
    }

    /**
     * Getter per il LiveData dei suggerimenti.
     * Questo risolve l'errore 'Cannot resolve method' nel Fragment.
     */
    public LiveData<List<String>> getCitySuggestionsLiveData() {
        return citySuggestionsLiveData;
    }



    // ==================================================================
    // --- METODI PRIVATI HELPER ---
    // ==================================================================



    /**
     * Metodo helper privato che centralizza TUTTA la logica di validazione (DRY).
     * Rispetta SRP: esegue solo la logica e restituisce un risultato (l'ID dell'errore).
     * Non gestisce la UI (non chiama validationError.setValue).
     * Rispetta Clean Code: ha 0 parametri perché legge lo stato interno del ViewModel.
     *
     * @return L'ID R.string dell'errore, o null se i dati sono validi.
     */
    private Integer findValidationErrorId() {
        // Legge i valori direttamente dai campi LiveData
        String fName = firstName.getValue();
        String lName = lastName.getValue();
        String uName = username.getValue();
        String eml = email.getValue();
        String pwd = password.getValue();
        String retypePwd = retypePassword.getValue();
        LocalDate dob = dateOfBirth.getValue();
        String pob = placeOfBirth.getValue();

        if (isEmpty(fName) || isEmpty(lName) || isEmpty(uName)) {
            return R.string.validation_error_firstname_lastname_username_required;
        }
        if (Validation.isNotValidEmail(eml)) {
            return R.string.validation_error_email_invalid;
        }
        if (!Validation.isValidPwd(pwd)) {
            return R.string.validation_error_password_short;
        }
        if (!Objects.equals(pwd, retypePwd)) {
            return R.string.validation_error_passwords_no_match;
        }
        if (dob == null) {
            return R.string.validation_error_dob_required;
        }
        if (dob.isAfter(maxDateAllowed)) {
            return R.string.validation_error_dob_underage;
        }
        if (dob.isBefore(minDateAllowed)) {
            return R.string.validation_error_dob_overage;
        }
        if (isEmpty(pob)) {
            return R.string.validation_error_pob_required;
        }

        return null; // Nessun errore, validazione superata
    }

    /**
     * Metodo Helper privato pulito (Clean Code).
     */
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}