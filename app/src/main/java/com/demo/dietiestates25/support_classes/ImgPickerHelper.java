package com.demo.dietiestates25.support_classes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.utils.ResourceUtils;

/**
 * Classe Helper per astrarre la logica di selezione di un'immagine dalla galleria.
 * Utilizza il pattern ActivityResultLauncher per un'API moderna.
 * SRP: La sua unica responsabilità è "avviare il selettore di immagini e
 * restituire l'URI o un errore" tramite un'interfaccia (ImgPickerListener).
 */
public class ImgPickerHelper {
    private final FragmentActivity activity;
    private ActivityResultLauncher<Intent> imgPickL;
    private final ImgPickerListener listener;
    private Uri currImgUri;

    private static final String IMG_MIME_TYPE = "image/*";

    public interface ImgPickerListener {
        void onImgSelected(@NonNull Uri imgUri);
        void onImgError(@NonNull String errorMsg);
    }

    /**
     * Costruttore aggiornato: Rimuove il parametro 'Utility'.
     */
    public ImgPickerHelper(FragmentActivity activity, ImgPickerListener listener) {
        this.activity = activity;
        this.listener = listener;

        initializeImgPicker();
    }

    private void initializeImgPicker() {
        imgPickL = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if(data != null && data.getData() != null) {
                            currImgUri = data.getData();
                            activity.getContentResolver().takePersistableUriPermission(currImgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            listener.onImgSelected(currImgUri);
                        }
                    }
                    else {
                        String errorMsg = ResourceUtils.getXMLString(activity, R.string.no_photo);
                        listener.onImgError(errorMsg);
                    }
                }
        );
    }

    /**
     * Apre il selettore di immagini (galleria) del sistema.
     */
    public void openImgPicker() { // legacy method
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(IMG_MIME_TYPE);
        imgPickL.launch(intent);
    }
}