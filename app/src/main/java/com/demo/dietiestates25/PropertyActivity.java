package com.demo.dietiestates25;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.dietiestates25.features.auth.util.SessionManager;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.ShowMapDialog;
import com.demo.dietiestates25.support_classes.real_estate_ads.PropertyAdapterKeys;
import com.demo.dietiestates25.utils.FormatUtils;
import com.demo.dietiestates25.utils.ImageLoaderUtils;
import com.demo.dietiestates25.utils.IntentUtils;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.viewmodels.PropertyViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.databinding.ActivityPropertyBinding;

import java.util.Objects;

/* È la schermata di Dettaglio Immobile.
* È la schermata che l'utente (Cliente o Agente) visualizza dopo aver cliccato su un annuncio nella lista (HomeFragment).
* La sua unica responsabilità è visualizzare tutti i dettagli di un singolo annuncio immobiliare (RealEstateAd) che riceve tramite Intent.
* Gestisce anche le azioni relative a quel singolo annuncio, come "Fai un'offerta" (tramite il FAB) o "Vedi su mappa".
* Basso Accoppiamento: È ben disaccoppiata. Riceve i suoi dati tramite un Intent (un contratto standard di Android) e delega la logica di business (come "invia offerta") al PropertyViewModel.
* Non sa da chi è stata chiamata (potrebbe essere HomeFragment, TrackingActivity, ecc.). */

/* Perché propertyPrice è un errore? (YAGNI / Clean Code)
* Nel tuo metodo getIntentData(), salvi il prezzo in un campo private double propertyPrice.
* Tuttavia, nel metodo populateUI() (che abbiamo corretto), leggi di nuovo il prezzo dall'Intent e lo usi immediatamente. */

/**
 * Activity per la Visualizzazione dei Dettagli di un singolo Annuncio Immobiliare.
 * SRP: Mostrare i Dettagli e Orchestrare le azioni per un immobile (ad esempio, Fare Offerta).
 * BASSO ACCOPPIAMENTO: Riceve tutti i dati tramite Intent.
 */
public class PropertyActivity extends AppCompatActivity {

    private static final String TAG = "PropertyActivity";
    private ActivityPropertyBinding binding;

    /* Componenti */
    private DialogManager dialogManager;
    private PropertyViewModel propertyViewModel;
    private SessionManager sessionManager; // è necessario per inviare un'offerta

    /* Dati di Stato */
    private long propertyId;
    private String propertyTitle;
    private String address;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* SETUP PULITO (CLEAN CODE) */
        initializeComponents(); // Inizializza i componenti principali. Contiene già dialogManager e viewModel
        getIntentData(); // Legge i dati principali dall'Intent
        setupToolbar(); // Imposta la toolbar con i dati
        populateUI(); // Popola il resto della UI (usa l'Intent)
        setupListeners(); // Imposta tutti i listener
        observeViewModel(); // Osserva i LiveData



        /*
         * =======================================================
         * Inizio Logica di Popolamento UI (dal tuo codice)
         * =======================================================
         */


        // Logica di setup
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        dialogManager = new DialogManager(this);

        propertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);

        // Questi sono già letti in 'getIntentData()'
        propertyTitle = getIntent().getStringExtra("Title");
        propertyId = getIntent().getLongExtra("IdProperty", -1);

        binding.fabFilterTracking.setOnClickListener(view -> dialogManager.showDialogClientOffer(propertyTitle, propertyId, false,
                (pId, amount, notes) -> {
                    long userId = sessionManager.getUserId(); // Assumendo che SessionManager.getUserId() esista
                    if (userId == -1) { // Assumendo -1 se non loggato
                        Snackbar.make(binding.getRoot(), "Errore: Utente non loggato.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    propertyViewModel.sendOffer(userId, pId, amount, notes);
                }));

        // Osserva il risultato dal ViewModel
        propertyViewModel.getOfferResult().observe(this, event -> {
            Boolean success = event.getContentIfNotHandled(); // Spacchetta l'evento
            if (success != null) { // Controlla se l'evento è nuovo
                if (success) { // Controlla il valore boolean
                    Snackbar.make(binding.getRoot(), "Offerta Inviata con successo!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(binding.getRoot(), "Errore durante l'invio dell'offerta", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Inizializza i componenti principali (ViewModel e Helper).
     */
    private void initializeComponents() {
        dialogManager = new DialogManager(this);
        sessionManager = new SessionManager(this); // Inizializza SessionManager
        propertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
    }

    /**
     * Legge i dati chiave dall'Intent e li salva nei campi dell'Activity.
     */
    private void getIntentData() {
        Intent intent = getIntent();
        PropertyAdapterKeys keys = new PropertyAdapterKeys();

        propertyId = intent.getLongExtra("IdProperty", -1);
        propertyTitle = IntentUtils.getStringIntent(intent, keys.getTitle());
        address = intent.getStringExtra(keys.getAddress());

        Log.d(TAG, "Activity aperta per immobile: " + propertyTitle + " (ID: " + propertyId + ")");
    }

    /**
     * Imposta la Toolbar con il titolo e il pulsante Indietro.
     */
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(propertyTitle);
        }
    }

    /**
     * Popola tutti i campi della UI con i dati ricevuti dall'Intent.
     * SRP: La sua Sola Responsabilità è riempire la UI.
     */
    private void populateUI() {
        Log.d(TAG, "Populating UI...");
        Intent intent = getIntent();
        PropertyAdapterKeys keys = new PropertyAdapterKeys();

        ImageView recImgV = findViewById(R.id.det_img);
        TextView classifyPropertyV = findViewById(R.id.det_classifyProperty);
        TextView descV = findViewById(R.id.det_description);
        TextView priceV = findViewById(R.id.det_price);
        TextView dimV = findViewById(R.id.det_dimensions);
        TextView addressV = findViewById(R.id.det_address);
        TextView roomsV = findViewById(R.id.det_rooms);
        TextView levelV = findViewById(R.id.det_level);
        TextView elevatorV = findViewById(R.id.det_elevator);
        TextView energyClassV = findViewById(R.id.det_energyClass);
        TextView servicesV = findViewById(R.id.det_services);
        ChipGroup chipPOI = findViewById(R.id.chips);
        Button fullDescBtn = findViewById(R.id.full_descriptionBtn);

        String imgUrl = IntentUtils.getStringIntent(intent, keys.getCoverImg());
        String title = IntentUtils.getStringIntent(intent, keys.getTitle());
        String city = IntentUtils.getStringIntent(intent, keys.getNameCity());
        String region = IntentUtils.getStringIntent(intent, keys.getNameRegion());
        String description = IntentUtils.getStringIntent(intent, keys.getDescription());
        address = IntentUtils.getStringIntent(intent, keys.getAddress());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(city + " - " + ResourceUtils.getXMLString(this, R.string.region) + " " + region);
        }

        ImageLoaderUtils.loadImgFromUrl(recImgV, imgUrl, R.drawable.no_img);

        setRecTxt(classifyPropertyV, IntentUtils.getStringIntent(intent, keys.getClassifyProp()));
        setRecTxt(descV, description);
        setRecTxt(priceV, FormatUtils.formatPrice(IntentUtils.getDoubleIntent(intent, keys.getPrice())));
        setRecTxt(addressV, address);
        setRecTxt(dimV, IntentUtils.getIntIntent(intent, keys.getDimensions())+" mq");
        setRecTxt(roomsV, IntentUtils.getIntIntent(intent, keys.getRooms())+"");
        setRecTxt(levelV, IntentUtils.getIntIntent(intent, keys.getLevel())+"°");
        setRecTxt(elevatorV, IntentUtils.getStringIntent(intent, keys.getElevator()));
        setRecTxt(energyClassV, IntentUtils.getStringIntent(intent, keys.getEnergyClass()));
        setRecTxt(servicesV, IntentUtils.getStringIntent(intent, keys.getServices()));

        boolean schools = IntentUtils.getBooleanIntent(intent, keys.getNearSchools());
        boolean parks = IntentUtils.getBooleanIntent(intent, keys.getNearParks());
        boolean transports = IntentUtils.getBooleanIntent(intent, keys.getNearPublicTransport());

        String schoolsName = ResourceUtils.getXMLString(this, R.string.near_schools);
        String parksName = ResourceUtils.getXMLString(this, R.string.near_parks);
        String transportName = ResourceUtils.getXMLString(this, R.string.near_public_transport);

        if (schools) addChips(chipPOI, this, new String[]{schoolsName});
        if (parks) addChips(chipPOI, this, new String[]{parksName});
        if (transports) addChips(chipPOI, this, new String[]{transportName});

        if (!schools && !parks && !transports) {
            String[] nothingPOI = {ResourceUtils.getXMLString(this, R.string.no_poi_nearby)};
            addChips(chipPOI, this, nothingPOI);
        }

        alignmentSingleChip(chipPOI);

        showFullDescription(fullDescBtn, keys, title, city, region, description);
    }

    /**
     * Imposta tutti i listener per i click.
     */
    private void setupListeners() {
        ImageButton imageBtn = findViewById(R.id.map);
        openMap(imageBtn); // Listener per il pulsante Mappa

        // Listener per il FAB (Floating Action Button) per fare un'offerta
        binding.fabFilterTracking.setOnClickListener(view -> {
            Log.d(TAG, "FAB 'Fai Offerta' cliccato.");
            dialogManager.showDialogClientOffer(propertyTitle, propertyId, false,
                    (pId, amount, notes) -> {
                        // Logica di invio offerta
                        long userId = sessionManager.getUserId();
                        if (userId == -1) {
                            Log.w(TAG, "Tentativo di offerta fallito: Utente non loggato (userId = -1)");
                            Snackbar.make(binding.getRoot(), "Errore: Utente non loggato.", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        Log.d(TAG, "Invio offerta al ViewModel... (UserID: " + userId + ", PropertyID: " + pId + ")");
                        propertyViewModel.sendOffer(userId, pId, amount, notes);
                    });
        });
    }

    /**
     * Imposta gli osservatori per i LiveData del ViewModel.
     */
    private void observeViewModel() {
        // Osserva il risultato dell'invio dell'offerta
        propertyViewModel.getOfferResult().observe(this, event -> {
            Boolean success = event.getContentIfNotHandled();
            if (success != null) {
                if (success) {
                    Log.i(TAG, "Offerta inviata con successo (ricevuto da ViewModel).");
                    Snackbar.make(binding.getRoot(), "Offerta Inviata con successo!", Snackbar.LENGTH_LONG).show();
                } else {
                    Log.w(TAG, "Errore invio offerta (ricevuto da ViewModel).");
                    Snackbar.make(binding.getRoot(), "Errore durante l'invio dell'offerta", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // Osserva lo stato di caricamento (es. durante l'invio dell'offerta)
        propertyViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                Log.d(TAG, "Operazione in corso...");
                // Qui potresti mostrare una ProgressBar
            } else {
                Log.d(TAG, "Operazione terminata.");
                // Qui potresti nascondere la ProgressBar
            }
        });
    }

    /* METODI HELPER */

    private static void setRecTxt(TextView txtV, String txt) {
        txtV.setText(txt);
    }

    private static void addChips(ChipGroup chipG, Context context, String[] poi) {
        for(String all_poi : poi) {
            Chip c = new Chip(context);
            c.setText(all_poi);
            c.setClickable(false);
            c.setCheckable(false);
            chipG.addView(c);
        }
    }

    private static void alignmentSingleChip(ChipGroup chipG) {
        if(chipG.getChildCount() == 1) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) chipG.getLayoutParams();
            params.gravity = Gravity.CENTER;
            chipG.setLayoutParams(params);
        }
    }

    private void showFullDescription(Button btn, PropertyAdapterKeys keys, String title, String city, String region, String description) {
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(PropertyActivity.this, FullDescPropActivity.class);
            intent.putExtra(keys.getTitle(), title);
            intent.putExtra(keys.getNameCity(), city);
            intent.putExtra(keys.getNameRegion(), region);
            intent.putExtra(keys.getDescription(), description);
            this.startActivity(intent);
        });
    }

    private void openMap(ImageButton imageBtn) {
        imageBtn.setOnClickListener(view -> {
            ShowMapDialog dialog = new ShowMapDialog(this, address);
            dialog.show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}