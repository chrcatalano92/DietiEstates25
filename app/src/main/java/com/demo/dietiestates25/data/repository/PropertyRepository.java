package com.demo.dietiestates25.data.repository;

import android.content.Context;
import android.net.Uri;

import com.demo.dietiestates25.data.dto.property.PropertyCreationDTO;
import com.demo.dietiestates25.data.dto.property.PropertyResponseDTO;
import com.demo.dietiestates25.support_classes.real_estate_ads.RealEstateAd;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.utils.LogUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Repository per la Gestione dei Dati degli Annunci Immobiliari.
 * SRP: Unica fonte di verità per i dati, astrae l'origine (Rete/DB).
 */
public class PropertyRepository {

    private static final String CLASS_TAG = "PropertyRepository";

    private final PropertyServiceAPI propertyApiService;
    private final Context context; // Salva il context per l'upload di file

    public PropertyRepository(Context context) {
        this.context = context.getApplicationContext();
        this.propertyApiService = RetrofitClient.getInstance(context).create(PropertyServiceAPI.class);
    }

    public Call<List<RealEstateAd>> getAllProperties() {
        return propertyApiService.getAllProperties();
    }

    public Call<List<RealEstateAd>> searchProperties(String query) {
        return propertyApiService.searchProperties(query);
    }

    /**
     * Gestisce la creazione dell'immobile.
     * Converte il DTO in una stringa JSON e prepara l'immagine (Uri) per un upload multipart.
     */
    public Call<PropertyResponseDTO> createProperty(PropertyCreationDTO dto, Uri imageUri) {

        // 1. INLINING: Prepara l'immagine (file) per l'upload. Non passiamo "imageFile"
        MultipartBody.Part imagePart = prepareFilePart(imageUri);
        if (imagePart == null && imageUri != null) {
            // Se imageUri non è nullo ma la preparazione fallisce, gestisci l'errore
            // (In questo caso, il ViewModel lo rileverà come errore di rete, che è ok)
            LogUtils.logError(CLASS_TAG, "Creazione di imagePart fallita.");
            // Potresti voler restituire un 'Call' fallito artificialmente qui
        }

        // 2. Converte il DTO in una stringa JSON
        Gson gson = new Gson();
        String dtoString = gson.toJson(dto);

        // 3. Crea il RequestBody per la stringa JSON
        RequestBody dtoPart = RequestBody.create(dtoString, MediaType.parse("application/json; charset=utf-8"));

        // 4. Chiama l'API
        return propertyApiService.createProperty(dtoPart, imagePart);
    }

    /**
     * Metodo helper privato per convertire un Uri in un MultipartBody.Part per l'upload di file.
     * L'unico posto in cui viene usato questo metodo è nel metodo createProperty().
     * Il parametro 'partName' è inutile, perché dà l'illusione che il metodo sia generico, ma in realtà è usato SOLO per un compito specifico.
     * La classe createProperty deve sapere un dettaglio implementativo (il nome "imageFile") che non dovrebbe essere di sua competenza.
     * Quindi, 'partName' rimosso.
     */
    private MultipartBody.Part prepareFilePart(Uri fileUri) {
        if (fileUri == null) {
            LogUtils.logError(CLASS_TAG, "Tentativo di upload senza immagine (imageUri è nullo)");
            return null; // O gestisci come errore
        }

        File tempFile;

        try (InputStream inputStream = context.getContentResolver().openInputStream(fileUri)) {

            /*
             * Controlla se l'inputStream è valido prima di usarlo.
             */
            if (inputStream == null) {
                LogUtils.logError(CLASS_TAG, "Impossibile aprire InputStream per l'URI: " + fileUri);
                return null;
            }

            // 1. Qui crei un file temporaneo, es: "upload12345.tmp"
            tempFile = File.createTempFile("upload", ".tmp", context.getCacheDir());
            tempFile.deleteOnExit();

            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int read;
                // La read() è ora sicura perché inputStream non è nullo
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
            } // outputStream si chiude automaticamente qui
        }
        /* inputStream si chiude automaticamente qui */
        catch (Exception e) {
            /*
             * CORREZIONE (Logging):
             * Usa LogUtils per un logging robusto invece di e.printStackTrace().
             */
            LogUtils.logError(CLASS_TAG, "Errore durante la preparazione del file: " + e.getMessage(), e);
            return null;
        }



        // --- Questa parte è dopo il try-with-resources ---

        // Ottieni il MimeType (es. "image/jpeg")
        String mimeType = context.getContentResolver().getType(fileUri);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Fallback
        }

        // 3. Qui stai caricando il file "upload.tmp" (vuoto!), non "upload12345.tmp"
        RequestBody requestFile = RequestBody.create(tempFile, MediaType.parse(mimeType));

        /*
         * CORREZIONE (Inlining):
         * Il partName "imageFile" è ora hardcoded qui dentro.
         * Questo migliora SRP e incapsulamento.
         */
        return MultipartBody.Part.createFormData("imageFile", tempFile.getName(), requestFile);
    }
}