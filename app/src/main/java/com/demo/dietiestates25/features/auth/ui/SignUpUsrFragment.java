package com.demo.dietiestates25.features.auth.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;



import com.demo.dietiestates25.MainActivity;
import com.demo.dietiestates25.R;

import com.demo.dietiestates25.databinding.FragmentSignUpUsrBinding;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModelFactory;
import com.demo.dietiestates25.features.auth.viewmodel.AuthViewModel;
import com.demo.dietiestates25.fragments_main_activity.MenuFragment;
import com.demo.dietiestates25.utils.LocaleUtils;
import com.demo.dietiestates25.support_classes.ProgressBarHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.FragmentUtils;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class SignUpUsrFragment extends Fragment {
    public static final String CLASS_TAG = "SignUpUsrFragment";


    private FragmentSignUpUsrBinding binding;
    private AuthViewModel viewModel;
    private ArrayAdapter<String> cityAdapter;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* 'cityAdapter' è un componente che implementa il Pattern Adapter.
     * SRP: La sua Sola Responsabilità è fare da PONTE (o Adattatore) tra due componenti incompatibili:
     *  1. I DATI: "List<String>" (ad esempio, ["Napoli (NA)", "Nola (NA)"]) che arriva dal AuthViewModel.
     *  2. LA VIEW: L'AutoCompleteTextView (il menu a tendina) che sa SOLO come mostrare una lista di "viste".
     * L'ADAPTER prende la lista di stringhe e crea una "View" per ogni stringa, mostrandola all'utente nel menu a tendina*/


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpUsrBinding.inflate(inflater, container, false);

        AuthViewModelFactory viewModelFactory = new AuthViewModelFactory(requireActivity());
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AuthViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DialogManager dialogManager = new DialogManager(requireContext());
        View contentSignUp = binding.contentAccountSignUp.getRoot();
        ProgressBarHelper progressBarHelper = new ProgressBarHelper(requireActivity(), binding.progressCircular);

        binding.title.setText(R.string.sign_up_title);

        // Setup dell'adapter per l'autocomplete
        setupCityAutocomplete();

        // Setup di tutti i listener
        setupActionListeners();

        // Setup di tutti gli observer
        observeViewModel(dialogManager, progressBarHelper, contentSignUp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    /* HELPER METHOD */

    /**
     * Inizializza l'ArrayAdapter (che è un campo) e lo collega alla View.
     */
    private void setupCityAutocomplete() {
        MaterialAutoCompleteTextView placeOfBirthView = binding.contentAccountSignUp.placeOfBirth;

        // Inizializza il campo. 'requireContext()' qui è sicuro.
        cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);

        placeOfBirthView.setAdapter(cityAdapter);
    }

    /**
     * Imposta tutti i listener per i click e le modifiche del testo.
     */
    private void setupActionListeners() {
        binding.contentAccountSignUp.btnCancel.setOnClickListener(v ->
                FragmentUtils.replaceFragment(getParentFragmentManager(), new MenuFragment()));

        /* Dice SOLO al AuthViewModel */
        binding.contentAccountSignUp.nextBtn.setOnClickListener(v -> viewModel.onRegisterClicked());

        /* Listener per la Data di nascita */
        binding.contentAccountSignUp.dateOfBirth.setOnFocusChangeListener((v, hasFocus) -> {
            // Mostra il selettore solo quando il campo *riceve* il focus
            if (hasFocus) {
                showDatePickerDialog();
            }
        });

        // Aggiungiamo anche un OnClickListener per sicurezza (se l'utente clicca di nuovo quando ha già il focus)
        binding.contentAccountSignUp.dateOfBirth.setOnClickListener(v -> showDatePickerDialog());

        /*
         * ========= LISTENER MANCANTE AGGIUNTO QUI =========
         * Listener per il Luogo di Nascita. Questo collega l'input dell'utente al ViewModel, attivando il debouncer.
         */
        binding.contentAccountSignUp.placeOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Notifica il ViewModel della nuova query per il debouncing
                if (viewModel != null) {
                    viewModel.onCityQueryChanged(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    /**
     * Mostra un selettore di data con i vincoli 18-90 anni presi dal ViewModel.
     * Questo calendario (DatePickerDialog) gestisce automaticamente anni bisestili e mesi.
     */
    private void showDatePickerDialog() {

        /* Chiamata al metodo per fixare la lingua del DatePicker */
        forceAppLocale();

        // 1. Costruisci i Vincoli (18-90 anni)
        // Questo dice al calendario quali date rendere cliccabili.
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setStart(viewModel.minDateMillis) // Data minima (90 anni fa)
                .setEnd(viewModel.maxDateMillis)   // Data massima (18 anni fa)
                // Imposta il calendario affinché si apra di default alla data massima (18 anni fa)
                .setOpenAt(viewModel.maxDateMillis)
                // Aggiungi i validatori per sicurezza
                .setValidator(DateValidatorPointForward.from(viewModel.minDateMillis))
                .setValidator(DateValidatorPointBackward.before(viewModel.maxDateMillis))
                .build();

        // 2. Costruisci il MaterialDatePicker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_of_birth) // Titolo del dialogo
                .setCalendarConstraints(constraints)  // Applica i vincoli
                .build();

        // 3. Listener per quando l'utente preme "OK"
        datePicker.addOnPositiveButtonClickListener(selectionInMillis -> {
            // Converte i millisecondi (UTC) in un LocalDate
            LocalDate dob = Instant.ofEpochMilli(selectionInMillis)
                    .atZone(ZoneOffset.UTC) // Importante: il picker usa UTC
                    .toLocalDate();

            // Aggiorna il ViewModel (la nostra "unica fonte di verità")
            viewModel.dateOfBirth.setValue(dob);

            // Aggiorna la UI (il campo di testo)
            binding.contentAccountSignUp.dateOfBirth.setText(dob.format(dateTimeFormatter));

            // Togli il focus per permettere un nuovo click
            binding.contentAccountSignUp.dateOfBirth.clearFocus();
        });

        // 4. Listener per quando l'utente preme "Annulla"
        datePicker.addOnNegativeButtonClickListener(v -> {
            // Togli il focus
            binding.contentAccountSignUp.dateOfBirth.clearFocus();
        });

        // 5. Listener per quando l'utente chiude il dialogo (es. cliccando fuori)
        datePicker.addOnCancelListener(dialog -> {
            // Togli il focus
            binding.contentAccountSignUp.dateOfBirth.clearFocus();
        });

        // 6. Mostra il dialogo
        // Usiamo getParentFragmentManager() perché siamo in un Fragment
        datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    /**
     * Osserva tutti i LiveData esposti dal ViewModel.
     */
    private void observeViewModel(DialogManager dialogManager, ProgressBarHelper progressBarHelper, View contentSignUp) {
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(Boolean.TRUE.equals(isLoading)) {
                contentSignUp.setVisibility(View.INVISIBLE);
                progressBarHelper.showProgress();
            }
            else {
                progressBarHelper.hideProgress();
                contentSignUp.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getRegistrationResult().observe(getViewLifecycleOwner(), event -> {
            Integer msgId = event.getContentIfNotHandled();

            if(msgId != null) {
                String msg = requireContext().getString(msgId);

                if(msgId == R.string.registration_success) {
                    dialogManager.showSuccessDialog(R.string.registration_title, msg, MainActivity.class);

                }
                else {
                    dialogManager.showErrorDialog(CLASS_TAG, R.string.registration_title, msg);
                }
            }
        });

        viewModel.getRegistrationError().observe(getViewLifecycleOwner(), event -> {
            String errorMsg = event.getContentIfNotHandled();

            if(errorMsg != null) {
                String formattedMsg = requireContext().getString(R.string.registration_error_generic, errorMsg);
                dialogManager.showErrorDialog(CLASS_TAG, R.string.registration_title, formattedMsg);
            }
        });

        viewModel.getValidationError().observe(getViewLifecycleOwner(), event -> {
            // 1. Ricevi l'ID della risorsa (un Integer) dall'evento.
            Integer errorMsgResId = event.getContentIfNotHandled();

            if (errorMsgResId != null) {
                // 2. Usa il contesto del Fragment (getString) per tradurre l'ID in una stringa.
                String errorMsg = getString(errorMsgResId);

                // 3. Mostra la stringa tradotta all'utente.
                dialogManager.showErrorDialog(CLASS_TAG, R.string.registration_title, errorMsg);
            }
        });

        // Aggiorna l'adapter dell'autocomplete
        viewModel.getCitySuggestionsLiveData().observe(getViewLifecycleOwner(), suggestions -> {
            if (cityAdapter != null) {
                cityAdapter.clear();
                cityAdapter.addAll(suggestions);
                cityAdapter.notifyDataSetChanged();
            }
        });
    }


    /* METODI SUB-HELPER PRIVATI */

    /**
     * AGGIUNTO: Metodo helper di Giorgio per forzare la lingua italiana/inglese
     * sul DatePicker, ignorando quella del telefono.
     */
    private void forceAppLocale() {
        try {
            String currentLanguage = LocaleUtils.getCurrentLanguage(requireContext());
            LocaleListCompat localeListCompat = LocaleListCompat.forLanguageTags(currentLanguage);
            AppCompatDelegate.setApplicationLocales(localeListCompat);
        }
        catch (IllegalArgumentException e) {
            // Aggiungi le stringhe di errore in strings.xml se non ci sono
            Log.e(CLASS_TAG, "Errore lingua DatePicker (IllegalArgument): " + e.getMessage());
        }
        catch (IllegalStateException e) {
            Log.e(CLASS_TAG, "Errore lingua DatePicker (IllegalState): " + e.getMessage());
        }
        catch (Exception e) {
            // Logga l'errore ma non fa crashare l'app
            Log.e(CLASS_TAG, "Errore durante l'impostazione della lingua del DatePicker: " + e.getMessage());
        }
    }
}