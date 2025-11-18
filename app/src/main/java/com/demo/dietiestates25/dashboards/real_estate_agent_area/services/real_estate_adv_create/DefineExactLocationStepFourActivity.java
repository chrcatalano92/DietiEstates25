package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.READashboardActivity;
import com.demo.dietiestates25.databinding.ActivityDefineExactLocationStepFourBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.LocationHelper;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.viewmodels.CreatePropertyViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

/**
 * Activity per lo STEP 4 della Creazione di un annuncio.
 * SRP: Gestisce la selezione della posizione sulla mappa,
 * conferma finale e avvia il submit del ViewModel.
 */
public class DefineExactLocationStepFourActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "StepFourActivity";


    private ActivityDefineExactLocationStepFourBinding binding;
    private CreatePropertyViewModel viewModel;
    private DialogManager dialogManager;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDefineExactLocationStepFourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* 1, Inizializza Componenti (MVVM) */
        viewModel = new ViewModelProvider(this).get(CreatePropertyViewModel.class);
        dialogManager = new DialogManager(this);

        /* 2. BASSO ACCOPPIAMENTO: Leggi i dati dal ViewModel */
        address = viewModel.address.getValue(); // L'indirizzo inserito nello STEP 1.

        /* 3. Setup UI */
        setupToolbar();
        setupUI();
        setupMap();
        setupListeners();
        observeViewModel(); // Osserva il risultato finale
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        /* Cerca di centrare la mappa sull'indirizzo inserito nello STEP 1. */
        if(address != null && !address.isEmpty()) {
            LocationHelper.getLocationFromAddress(dialogManager, this, googleMap, address, this, 15, true);
        }

        // AGGIUNTO: Logica fondamentale dello Step 4
        googleMap.setOnMapClickListener(latLng -> {
            Log.d(TAG, "Posizione selezionata dall'utente: " + latLng.latitude + ", " + latLng.longitude);

            // 1. Salva le coordinate nel ViewModel condiviso
            viewModel.latitude.setValue(latLng.latitude);
            viewModel.longitude.setValue(latLng.longitude);

            // 2. Aggiorna la UI della mappa
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Posizione Selezionata"));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Gestisce la freccia "indietro" nella Toolbar
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }


    /* METODI HELPER PRIVATI */


    /* per il metodo onCreate()*/

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.prop_listing_title);
    }

    private void setupUI() {
        // Usa ResourceUtils
        String step4 = ResourceUtils.getXMLString(this, R.string.step) + " 4 " + ResourceUtils.getXMLString(this, R.string.of) + " 4";
        binding.content.steps.step.setText(step4);
        binding.content.steps.stepTitle.setText(R.string.step4_title);

        // Testo del bottone (come richiesto)
        binding.content.backAndNextBtn.nextBtn.setText(R.string.publish_ad);
    }

    /* vecchio metodo privato void 'showMapPin' che usava il FragmentManager.
    * Ora, si chiama 'setupMap' che gestisce la mappa. */
    private void setupMap() {
        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        if (smf == null) {
            smf = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.map_container, smf)
                    .commit();
        }
        smf.getMapAsync(this);
    }

    private void setupListeners() {
        // CORRETTO: Usa il dispatcher non deprecato
        binding.content.backAndNextBtn.backBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        // CORRETTO: Chiama la logica di conferma locale
        binding.content.backAndNextBtn.nextBtn.setOnClickListener(view -> showConfirmationDialog());
    }

    /**
     * Osserva i LiveData del ViewModel per reagire al risultato del submit.
     */
    private void observeViewModel() {
        viewModel.getCreationResult().observe(this, event -> {
            Boolean success = event.getContentIfNotHandled();
            if (success != null) {
                if (success) {
                    Log.i(TAG, "Annuncio creato con successo.");
                    dialogManager.showSuccessDialog(
                            R.string.prop_listing_title,
                            R.string.property_ad_saved,
                            READashboardActivity.class // Torna alla dashboard
                    );
                    // Chiude tutte le activity del wizard (Step 1, 2, 3, 4)
                    finishAffinity();
                } else {
                    Log.e(TAG, "Errore durante la creazione dell'annuncio.");
                    // L'errore specifico è già nel LiveData 'errorMessage'
                    // Questo è solo un fallback
                    dialogManager.showErrorDialog(TAG, "Errore Creazione", "Impossibile salvare l'annuncio.");
                }
            }
        });

        // Osserva i messaggi di errore (es. validazione)
        viewModel.getErrorMessageResId().observe(this, event -> {
            String msg = event.getContentIfNotHandled();
            if (msg != null) {
                dialogManager.showErrorDialog(TAG, "Errore di Validazione", msg);
            }
        });

        // (Opzionale) Osserva lo stato di caricamento
        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                // Qui potresti mostrare una ProgressBar
                Log.d(TAG, "Caricamento in corso...");
            } else {
                Log.d(TAG, "Caricamento terminato.");
            }
        });
    }


    /* per il metodo onMapReady() */

    /**
     * Mostra il dialogo di conferma finale.
     * Sostituisce la logica di InputManagerStepFour.
     */
    private void showConfirmationDialog() {
        String question = ResourceUtils.getXMLString(this, R.string.data_confirmation);

        dialogManager.showQuestionDialog(question, confirmed -> {
            if (confirmed) {
                // Utente ha cliccato "SI"
                Log.d(TAG, "Conferma invio dati... Chiamo viewModel.submitProperty()");

                // 3. (Opzionale) Validazione finale prima dell'invio
                if (viewModel.latitude.getValue() == null || viewModel.longitude.getValue() == null) {
                    dialogManager.showErrorDialog(TAG, "Posizione Mancante", "Devi selezionare un punto sulla mappa cliccando.");
                    return;
                }

                // 4. Avvia il submit (il ViewModel ha già tutti i dati)
                viewModel.submitProperty();

            } else {
                // Utente ha cliccato "NO" o [X], il dialogo si chiude
                Log.d(TAG, "Invio annullato dall'utente.");
            }
        });
    }
}