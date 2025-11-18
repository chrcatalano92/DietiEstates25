package com.demo.dietiestates25.support_classes.retrofit_utility.property_save.images_upload;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* Questa classe NON è una classe Utils statica. È una classe helper non statica (di istanza).
Ha uno stato (i campi final come activity, uploadListener, propertyId) e una singola azione (uploadImgToServer).
La sua Sola Responsabilità (SRP): è gestire il processo di upload di una singola immagine per un immobile.
Astrae la logica di:
  1. Creare un file temporaneo.
  2. Copiare l'URI nel file.
  3. Preparare la chiamata 'Multipart' di Retrofit.
  4. Eseguire la chiamata.
  5. Gestire le callback (onSuccess / onFailure) e pulire il file temporaneo. */

/**
 * Classe Helper (non statica) che gestisce la logica di upload di un file immagine.
 * SRP: Incapsula la creazione di file temporanei, la preparazione di Multipart
 * e la gestione delle callback di rete per un singolo upload.
 */
public class ImgUploadHelper {
    private final Activity activity;
    private final ImgUploadListener uploadListener;
    private final PropertyServiceAPI apiService;
    private final Long propertyId;
    private final Uri currImgUri;
    private static final String TMP_FILE_PREFIX = "upload";
    private static final String TMP_FILE_EXTENSION = ".jpg";
    private static final String IMG_MIME_TYPE = "image/*";
    private static final String TXT_MIME_TYPE = "text/plain";

    public interface ImgUploadListener {
        void onUploadImgSuccess(@NonNull String response);
        void onUploadImgFailure(@NonNull String errMsg);
    }

    public ImgUploadHelper(Activity activity, ImgUploadListener uploadListener, Long propertyId, Uri currUri) {
        this.activity = activity;
        this.uploadListener = uploadListener;
        this.apiService = RetrofitClient.getRetrofitInstance(activity);
        this.propertyId = propertyId;
        this.currImgUri = currUri;
    }

    public void uploadImgToServer(String folderName, String filename) {
        if(currImgUri == null) {
            uploadListener.onUploadImgFailure(ResourceUtils.getXMLString(activity, R.string.no_photo));
        }
        else {
            try {
                File tmpFile = createTmpFile();

                if(!copyUriContentToFile(currImgUri, tmpFile)) {
                    deleteTmpFile(tmpFile);
                    uploadListener.onUploadImgFailure(ResourceUtils.getXMLString(activity, R.string.copy_img_file_failed));
                }

                uploadRequest(tmpFile, folderName, filename);
            } catch (IOException e) {
                uploadListener.onUploadImgFailure(ResourceUtils.getXMLString(activity, R.string.upload_img_failure) + ": " + e.getMessage());
            }
        }
    }

    private File createTmpFile() throws IOException {
        return File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_EXTENSION, activity.getCacheDir());
    }

    private boolean copyUriContentToFile(@NonNull Uri uri, @NonNull File file) throws IOException {
        try (
                InputStream is = activity.getContentResolver().openInputStream(uri);
                FileOutputStream os = new FileOutputStream(file)
        ) {
            if (is == null) {
                return false;
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return true;
        }
    }

    private void uploadRequest(@NonNull File file, @NonNull String folderName, @NonNull String filename) {
        RequestBody requestFile = RequestBody.create(file, MediaType.get(IMG_MIME_TYPE));

        // Usa il metodo 'create' non deprecato (File, MediaType)
        MultipartBody.Part imgPart = MultipartBody.Part.createFormData(
                "img",
                filename,
                requestFile
        );

        // Usa il metodo 'create' non deprecato (String, MediaType)
        RequestBody propertyIdBody = RequestBody.create(String.valueOf(propertyId), MediaType.get(TXT_MIME_TYPE));
        RequestBody folderPart = RequestBody.create(folderName, MediaType.get(TXT_MIME_TYPE));

        Call<ResponseBody> call = this.apiService.uploadImg(imgPart, propertyIdBody, folderPart);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                handleUploadResponse(response, file);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                handleUploadFailure(t, file);
            }
        });
    }

    private void handleUploadResponse(@NonNull Response<ResponseBody> response, @NonNull File tmpFile) {
        deleteTmpFile(tmpFile);

        if(response.isSuccessful()) {
            uploadListener.onUploadImgSuccess(ResourceUtils.getXMLString(activity, R.string.upload_img_completed));
        }
        else {
            uploadListener.onUploadImgFailure(ResourceUtils.getXMLString(activity, R.string.upload_img_failure));
        }
    }

    private void handleUploadFailure(@NonNull Throwable t, @NonNull File tmpFile) {
        deleteTmpFile(tmpFile);
        String error = t.getMessage() != null ? t.getMessage() : ResourceUtils.getXMLString(activity, R.string.upload_img_failure);
        uploadListener.onUploadImgFailure(error);
    }

    private void deleteTmpFile(@NonNull File file) {
        // 1. SE l'API è 26 o superiore...
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                // 2. ...USA IL METODO MODERNO E PREFERITO
                Files.delete(file.toPath());
            } catch (IOException e) {
                String error = ResourceUtils.getXMLString(activity, R.string.err_delete_tmp_file) + ": " + e.getMessage();
                uploadListener.onUploadImgFailure(error);
            }
        }
        // 3. ALTRIMENTI (se l'API è 25 o inferiore)...
        else {
            if (!file.delete()) { // NOSONAR
                String error = ResourceUtils.getXMLString(activity, R.string.err_delete_tmp_file_old_api);
                uploadListener.onUploadImgFailure(error);
            }
        }
    }
}