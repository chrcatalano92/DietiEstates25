package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityPropertyListingCreateStepOneBinding;
import com.demo.dietiestates25.support_classes.ImgPickerHelper;
import com.demo.dietiestates25.support_classes.SlidePanelHelper;
import com.demo.dietiestates25.support_classes.ToggleStateHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.StringPickerHelper;
import com.demo.dietiestates25.utils.ImageLoaderUtils;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.viewmodels.CreatePropertyViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* Questa è l'Activity (o Fragment) che gestisce la UI del primo step del wizard (es. inserimento di titolo, prezzo, descrizione, immagine).
Questa classe "scrive" i dati nel CreatePropertyViewModel. */

/**
 * Activity per lo STEP 1 della Creazione di un annuncio immobiliare.
 * Gestisce l'input di base.
 * SRP: La sua Sola Responsabilità (SRP): Collezionare i dati dello STEP 1, validarli e salvarli nel CreatePropertyViewModel condiviso.
 */
public class PropertyListingCreateStepOneActivity extends AppCompatActivity implements ImgPickerHelper.ImgPickerListener {
    private static final String TAG = "StepOneActivity";
    private ActivityPropertyListingCreateStepOneBinding binding;
    private CreatePropertyViewModel viewModel;
    private DialogManager dialogManager;
    private ImgPickerHelper imgPickerHelper;

    // Cache per i dati dello StringPicker (DRY)
    private String[] energyClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPropertyListingCreateStepOneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Inizializzazione pulita */
        viewModel = new ViewModelProvider(this).get(CreatePropertyViewModel.class);
        dialogManager = new DialogManager(this);
        imgPickerHelper = new ImgPickerHelper(this, this);

        setupToolbar();
        setupUI();
        setupListeners();
        observeViewModel(); // Ripopola la UI se i dati esistono già
    }



    /* METODI HELPER PRIVATI */

    // Logica della Toolbar con binding
    private void setupToolbar() {
        Log.d(TAG, "Configurazione della Toolbar.");
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.prop_listing_title);
    }

    // Logica di Setup della UI con binding.
    private void setupUI() {
        Log.d(TAG, "Configurazione della UI iniziale.");
        String step1 = ResourceUtils.getXMLString(this, R.string.step) + " 1 " + ResourceUtils.getXMLString(this, R.string.of) + " 4";
        binding.content.steps.step.setText(step1);
        binding.content.steps.stepTitle.setText(R.string.choose_photo);

        // Configurazione del picker per la Classe Energetica
        this.energyClasses = StringPickerHelper.energyClassData();

        binding.content.picker.stringPicker.setMinValue(0);
        binding.content.picker.stringPicker.setMaxValue(energyClasses.length - 1);
        binding.content.picker.stringPicker.setDisplayedValues(energyClasses);
        
        String defaultEnergyClass = viewModel.energyClass.getValue();
        int defaultIndex = findIndex(energyClasses, defaultEnergyClass);
        binding.content.picker.stringPicker.setValue(defaultIndex);

        // Impedisce alla tastiera di spostare il Layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    // Logica del Listener centralizzata ed è con binding.
    private void setupListeners() {
        Log.d(TAG, "Impostazione dei listener per i componenti UI.");
        // Scegli foto
        binding.content.choosePhotoBtn.setOnClickListener(view -> imgPickerHelper.openImgPicker());

        // Mostra(Nascondi pannello immagine
        SlidePanelHelper.toggleImgVisibility(binding.content.slidePanel, binding.content.showHideImgBtn);

        // Gestione del Toggle Button per l'ascensore
        ToggleStateHelper.toggleState(binding.content.toggleGroup, binding.content.btnYes, binding.content.btnNo);

        // Gestione del NumberPicker di Classe Energetica.
        // Aggiorna il ViewModel *immediatamente* quando il picker cambia
        binding.content.picker.stringPicker.setOnValueChangedListener((picker, oldVal, newVal) -> viewModel.energyClass.setValue(energyClasses[newVal]));

        // Pulsante AVANTI.
        binding.content.sendBtn.setOnClickListener(view -> validateAndProceedToStepTwo());
    }

    // Ripopola la UI se i dati esistono già nel ViewModel (esempio, dopo rotazione schermo)
    private void observeViewModel() {
        Log.d(TAG, "Impostazione degli observer per il ViewModel.");
        viewModel.imageUri.observe(this, uri -> {
            if (uri != null) {
                ImageLoaderUtils.loadImgFromUri(this, uri, binding.content.chosenImgFile);
                SlidePanelHelper.visibilityManager(binding.content.slidePanel, binding.content.showHideImgBtn, View.VISIBLE, R.drawable.ShowImg);
            }
        });
        viewModel.title.observe(this, text -> setTextIfDifferent(binding.content.titleEditTxt, text));
        viewModel.description.observe(this, text -> setTextIfDifferent(binding.content.descriptionEditTxt, text));

        viewModel.price.observe(this, priceValue -> {
            if (priceValue != null) {
                binding.content.priceEditTxt.setText(String.valueOf(priceValue));
            }
        });

        viewModel.dimensions.observe(this, text -> setTextIfDifferent(binding.content.dimensionsEditTxt, text));
        viewModel.rooms.observe(this, text -> setTextIfDifferent(binding.content.roomsEditTxt, text));
        viewModel.level.observe(this, text -> setTextIfDifferent(binding.content.levelEditTxt, text));
        viewModel.address.observe(this, text -> setTextIfDifferent(binding.content.addressEditTxt, text));

        viewModel.services.observe(this, services -> {
            if (services != null) {
                setTextIfDifferent(binding.content.servicesEditText, TextUtils.join(", ", services));
            }
        });

        viewModel.hasElevator.observe(this, hasElevator -> {
            if (hasElevator != null) {
                if (hasElevator) {
                    binding.content.btnYes.setChecked(true);
                } else {
                    binding.content.btnNo.setChecked(true);
                }
            }
        });
        
        viewModel.energyClass.observe(this, value -> {
            int index = findIndex(energyClasses, value);
            if (binding.content.picker.stringPicker.getValue() != index) {
                binding.content.picker.stringPicker.setValue(index);
            }
        });
        // aggiungere altri observe per gli altri campi
    }



    /**
     * Questa è la nuova funzione che sostituisce 'InputManagerStepOne'.
     * La logica di validazione e navigazione è ora responsabilità dell'Activity.
     */
    private void validateAndProceedToStepTwo() {
        /*
         * CORREZIONE: Errore "language level"
         * Sostituito "Log.i(TAG. "Testo");" con "Log.i(TAG, "Testo");"
         */
        Log.i(TAG, "Tentativo di validazione e passaggio allo STEP 2.");

        if (viewModel.imageUri.getValue() == null) {
            dialogManager.showErrorDialog(TAG, R.string.no_photo);
            return;
        }

        if (isFieldEmpty(binding.content.titleEditTxt, "Titolo")) return;
        if (isFieldEmpty(binding.content.descriptionEditTxt, "Descrizione")) return;
        if (isFieldEmpty(binding.content.priceEditTxt, "Prezzo")) return;
        if (isFieldEmpty(binding.content.dimensionsEditTxt, "Dimensioni")) return;
        if (isFieldEmpty(binding.content.roomsEditTxt, "Stanze")) return;
        if (isFieldEmpty(binding.content.levelEditTxt, "Piano")) return;
        if (isFieldEmpty(binding.content.addressEditTxt, "Indirizzo")) return;
        // (La classe energetica ha un default, l'ascensore ha un default)


        // 2. Raccoglie e Salva i dati nel ViewModel (Basso Accoppiamento)
        Log.d(TAG, "Validazione superata. Salvo i dati nel ViewModel.");
        viewModel.title.setValue(getSafeString(binding.content.titleEditTxt));
        viewModel.description.setValue(getSafeString(binding.content.descriptionEditTxt));

        /*
         * CORREZIONE: "setValue()" vuoto. Converte il testo del prezzo in Double.
         */
        try {
            viewModel.price.setValue(Double.parseDouble(getSafeString(binding.content.priceEditTxt)));
        } catch (NumberFormatException e) {
            binding.content.priceEditTxt.setError("Formato prezzo non valido");
            return;
        }

        viewModel.address.setValue(getSafeString(binding.content.addressEditTxt));
        viewModel.dimensions.setValue(getSafeString(binding.content.dimensionsEditTxt));
        viewModel.rooms.setValue(getSafeString(binding.content.roomsEditTxt));
        viewModel.level.setValue(getSafeString(binding.content.levelEditTxt));


        /*
         * CORREZIONE: Errore di tipo (String vs List<String>)
         * Converte la stringa "Servizio1, Servizio2" in una List<String>.
         */
        String servicesString = getSafeString(binding.content.servicesEditText);
        List<String> servicesList = new ArrayList<>(Arrays.asList(servicesString.split("\\s*,\\s*"))); // Divide per virgola e trimma
        viewModel.services.setValue(servicesList);

        boolean hasElevator = binding.content.btnYes.isChecked();
        viewModel.hasElevator.setValue(hasElevator);

        // La classe energetica è già aggiornata dal listener, non serve salvarla qui.


        /*
         * CORREZIONE: Variabili duplicate e NullPointerException.
         * Le righe seguenti sono ridondanti (i dati sono già nel ViewModel) e
         * 'toString()' può causare NPE. Le rimuoviamo.
         */

        // salva tutti gli altri dati
//        String title = binding.content.titleEditTxt.getText().toString();
//        String desc = binding.content.descriptionEditTxt.getText().toString();
//        String services = binding.content.servicesEditText.getText().toString();
//        String address = binding.content.addressEditTxt.getText().toString();
        // raccogli tutti gli altri dati


        // 3. Naviga allo STEP 2
        Log.i(TAG, "Navigazione verso ClassifyNewPropertyStepTwoActivity.");
        startActivity(new Intent(this, ClassifyNewPropertyStepTwoActivity.class));
    }

    /* METODI HELPER PRIVATI */

    private boolean isFieldEmpty(TextInputEditText editText, String fieldName) {
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setError("Il campo '" + fieldName + "' è obbligatorio");
            return true;
        }
        return false;
    }

    /* METODO HELPER privato che controlla se il testo è null prima di chiamare toString(),
    * restituendo una stringa vuota in caso di null. Questo rende il codice sicuro e riutilizzabile (DRY) */
    private String getSafeString(TextInputEditText editText) {
        if (editText.getText() != null) {
            return editText.getText().toString();
        }
        return "";
    }

    /**
     * Helper per evitare di resettare il cursore del campo di testo
     * se l'utente sta ancora scrivendo (Clean Code).
     */
    private void setTextIfDifferent(TextInputEditText editText, String text) {
        if (editText.getText() == null || !editText.getText().toString().equals(text)) {
            editText.setText(text);
        }
    }

    private int findIndex(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) {
                return i;
            }
        }
        return 0; // Ritorna l'indice di default (es. "A4")
    }



    // Callback per ImgPickerHelper
    @Override
    public void onImgSelected(@NonNull Uri imgUri) {
        Log.d(TAG, "Immagine selezionata, salvo l'URI nel ViewModel: " + imgUri);
        viewModel.imageUri.setValue(imgUri);
        ImageLoaderUtils.loadImgFromUri(this, imgUri, binding.content.chosenImgFile);
        SlidePanelHelper.visibilityManager(binding.content.slidePanel, binding.content.showHideImgBtn, View.VISIBLE, R.drawable.ShowImg);
    }

    @Override
    public void onImgError(@NonNull String errorMsg) {
        Log.e(TAG, "Errore durante la selezione dell'immagine: " + errorMsg);
        dialogManager.showErrorDialog("ImgPickerHelper", errorMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}