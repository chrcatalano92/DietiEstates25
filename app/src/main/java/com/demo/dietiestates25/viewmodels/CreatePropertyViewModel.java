package com.demo.dietiestates25.viewmodels;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.dto.property.PropertyCreationDTO;
import com.demo.dietiestates25.data.dto.property.PropertyResponseDTO;
import com.demo.dietiestates25.data.repository.PropertyRepository;
import com.demo.dietiestates25.support_classes.arch.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* Ha la responsabilità di impostare il valore di default della classe energetica.
* Lui garantisce che il dato esista, anche se Agent non tocca il NumberPicker.
* Questa è la classe più importante del wizard.
* È il ViewModel condiviso che sopravvive mentre l'agente si sposta tra i vari step.
* La sua responsabilità (SRP) è mantenere lo stato e raccogliere tutti i dati (titolo, prezzo, immagine, posizione, ecc.) in un unico posto. */

/* CreatePropertyViewModel ha bisogno di due classi DTO per comunicare con il PropertyRepository:
* 1. PropertyCreationDTO.java: L'oggetto Java che GSON trasformerà in JSON da inviare al backend.
* 2. PropertyResponseDTO.java: L'oggetto Java in cui GSON inserirà la risposta JSON ricevuta dal backend. */

/**
 * Classe ViewModel condiviso per il flusso di creazione di un Annuncio Immobiliare in 4 STEP (ViewModel Wizard).
 * Mantiene lo Stato di tutti i dati raccolti durante il Wizard.
 * SRP: La sua Sola Responsabilità è gestire lo stato e la logica di business per la CREAZIONE di un nuovo immobile.
 */
public class CreatePropertyViewModel extends AndroidViewModel {

    private static final String TAG = "CreatePropertyVM";

    /* Il Repository per gestire la logica di rete/database */
    private final PropertyRepository propertyRepository;
    private final MutableLiveData<Event<Integer>> errorMessageResId = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> networkError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);



    // Dati raccolti dallo STEP 1
    public final MutableLiveData<Uri> imageUri = new MutableLiveData<>();
    public final MutableLiveData<String> title = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<Double> price = new MutableLiveData<>();
    public final MutableLiveData<String> dimensions = new MutableLiveData<>();
    public final MutableLiveData<String> rooms = new MutableLiveData<>();
    public final MutableLiveData<String> level = new MutableLiveData<>();
    public final MutableLiveData<List<String>> services = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<String> address = new MutableLiveData<>();
    public final MutableLiveData<Boolean> hasElevator = new MutableLiveData<>(false);
    public final MutableLiveData<String> energyClass = new MutableLiveData<>("A4");

    // Dati raccolti dallo STEP 2
    public final MutableLiveData<String> classifyProperty = new MutableLiveData<>();

    // Dati raccolti dallo STEP 3
    public final MutableLiveData<String> typeProperty = new MutableLiveData<>();

    // Dati raccolti dallo STEP 4
    public final MutableLiveData<Double> latitude = new MutableLiveData<>();
    public final MutableLiveData<Double> longitude = new MutableLiveData<>();

    // LiveData per il Risultato Finale
    private final MutableLiveData<Event<Boolean>> creationResult = new MutableLiveData<>();

    public CreatePropertyViewModel(@NonNull Application application) {
        super(application);
        this.propertyRepository = new PropertyRepository(application.getApplicationContext());
    }


    /* GETTERS PER I RISULTATI */

    /**
     * Il Fragment/Activity osserva questo per sapere quando la creazione è completata.
     */
    public LiveData<Event<Boolean>> getCreationResult() { return creationResult; }

    /**
     * Osservato dalla UI per mostrare/nascondere una ProgressBar.
     */
    public LiveData<Boolean> isLoading() { return isLoading; }

    /**
     * Osservato dalla UI per mostrare messaggi di errore (es. validazione, rete).
     */
    public LiveData<Event<Integer>> getErrorMessageResId() { return errorMessageResId; }

    public LiveData<Event<String>> getNetworkError() { return networkError; }



    // --- METODI DI BUSINESS (chiamati dai Fragment/Activity) ---

    /**
     * Aggiunge un servizio (es. "Portineria") alla lista.
     * Chiamato dalla UI (es. un Chip) nello STEP 1.
     */
    public void addService(String service) {
        List<String> currentServices = services.getValue();
        if (currentServices != null && !currentServices.contains(service)) {
            currentServices.add(service);
            services.setValue(currentServices); // Notifica gli observer
        }
    }

    /**
     * Rimuove un servizio dalla lista.
     * Chiamato dalla UI (es. un Chip) nello STEP 1.
     */
    public void removeService(String service) {
        List<String> currentServices = services.getValue();
        if (currentServices != null && currentServices.contains(service)) {
            currentServices.remove(service);
            services.setValue(currentServices); // Notifica gli observer
        }
    }

    /**
     * Chiamato dall'Activity finale (STEP 4) per inviare TUTTI I DATI AL BACKEND.
     * Questo metodo ora è completo e usa tutti i campi. La logica che sostituisce 'InputManagerStepFour' e 'RetrofitPropertySaveHelper'.
     */
    public void submitProperty() {
        // 1. VALIDAZIONE (Fail-Fast)
        if (!isDataValid()) {
            return;
        }

        Log.d(TAG, "Dati validati. Avvio l'upload...");
        isLoading.setValue(true);

        /* 2. RACCOGLI TUTTI I DATI dai MutableLiveData in un DTO */
        // (Assumendo che esista un DTO 'PropertyCreationDTO' che accetta questi dati)
        PropertyCreationDTO newProperty = new PropertyCreationDTO();
        newProperty.setTitle(title.getValue());
        newProperty.setDescription(description.getValue());
        newProperty.setPrice(price.getValue());
        newProperty.setDimensions(dimensions.getValue());
        newProperty.setRooms(rooms.getValue());
        newProperty.setLevel(level.getValue());
        newProperty.setServices(services.getValue());
        newProperty.setAddress(address.getValue());
        newProperty.setHasElevator(hasElevator.getValue());
        newProperty.setEnergyClass(energyClass.getValue());
        newProperty.setClassifyProperty(classifyProperty.getValue());
        newProperty.setTypeProperty(typeProperty.getValue()); // 'typeProperty' ora è usato
        newProperty.setLatitude(latitude.getValue()); // 'latitude' ora è usato
        newProperty.setLongitude(longitude.getValue()); // 'longitude' ora è usato
        // Imposta l'ID dell'agente nel DTO (se necessario)

        // 3. CHIAMA IL Repository per ESEGUIRE LA CHIAMATA DI RETE (che gestisce il multipart)
        // 'propertyRepository' ora è usato
        // (Assumendo che 'createProperty' gestisca l'upload dell'immagine e del DTO)
        propertyRepository.createProperty(newProperty, imageUri.getValue()).enqueue(new Callback<PropertyResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<PropertyResponseDTO> call, @NonNull Response<PropertyResponseDTO> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    Log.i(TAG, "Upload completato con successo.");
                    creationResult.setValue(new Event<>(true)); // Successo
                } else {
                    Log.e(TAG, "Errore upload (HTTP " + response.code() + ")");
                    creationResult.setValue(new Event<>(false));
                    networkError.setValue(new Event<>("Errore: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PropertyResponseDTO> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "Errore di rete durante l'upload.", t);
                creationResult.setValue(new Event<>(false));
                networkError.setValue(new Event<>(t.getMessage()));
            }
        });
    }

    /**
     * Metodo helper privato che centralizza la validazione finale (DRY).
     * @return true se tutti i dati sono validi, false altrimenti.
     */
    private boolean isDataValid() {
        if (imageUri.getValue() == null) {
            errorMessageResId.setValue(new Event<>(R.string.no_photo));
            return false;
        }
        if (TextUtils.isEmpty(title.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_title_required)); // Aggiungi questa stringa
            return false;
        }

        if (TextUtils.isEmpty(description.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_description_required)); // Aggiungi questa stringa
            return false;
        }

        if (price.getValue() == null) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_price_required)); // Aggiungi questa stringa
            return false;
        }

        if (TextUtils.isEmpty(dimensions.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_dimensions_required)); // Aggiungi questa stringa
            return false;
        }

        if (TextUtils.isEmpty(rooms.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_rooms_required)); // Aggiungi questa stringa
            return false;
        }

        if (TextUtils.isEmpty(level.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_level_required)); // Aggiungi questa stringa
            return false;
        }

        if (services.getValue() == null || services.getValue().isEmpty()) {
            errorMessageResId.setValue(new Event<>(R.string.nothing_choice)); // Riusa la stringa
            return false;
        }

        if (TextUtils.isEmpty(address.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_address_required)); // Aggiungi questa stringa
            return false;
        }

        if (hasElevator.getValue() == null) {
            errorMessageResId.setValue(new Event<>(R.string.nothing_choice)); // Riusa la stringa
            return false;
        }

        if (TextUtils.isEmpty(energyClass.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.nothing_choice)); // Riusa la stringa
            return false;
        }

        if (TextUtils.isEmpty(classifyProperty.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.nothing_choice)); // Riusa la stringa
            return false;
        }

        if (TextUtils.isEmpty(typeProperty.getValue())) {
            errorMessageResId.setValue(new Event<>(R.string.nothing_choice)); // Riusa la stringa
            return false;
        }

        if (latitude.getValue() == null || longitude.getValue() == null) {
            errorMessageResId.setValue(new Event<>(R.string.validation_error_position_required)); // Aggiungi questa stringa
            return false;
        }
        // ... aggiungi tutte le altre validazioni (prezzo, stanze, ecc.)

        return true;
    }
}
