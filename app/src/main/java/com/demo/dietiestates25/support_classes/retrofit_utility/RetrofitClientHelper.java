package com.demo.dietiestates25.support_classes.retrofit_utility;

import android.app.Activity;
import android.content.Context;

import com.demo.dietiestates25.support_classes.ProgressBarHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;

/**
 * Helper per inizializzare il client Retrofit in un thread separato
 * e gestire le callback di successo o errore sulla UI.
 * È disaccoppiato da 'Utility' e ottiene le risorse necessarie
 * tramite il 'Context'.
 */
public class RetrofitClientHelper {
    public static final String CLASS_TAG = "RetrofitClientHelper";
    private final Context context;
    private final Activity activity;
    private final DialogManager dialogManager;
    private final String userRole;
    private final ProgressBarHelper barHelper;

    public RetrofitClientHelper(Context context, Activity activity, String userRole, ProgressBarHelper barHelper) {
        this.context = context;
        this.activity = activity;
        this.userRole = userRole;
        this.barHelper = barHelper;
        this.dialogManager = new DialogManager(context);
    }

    public interface RetrofitCallback {
        void onRetrofitInitialized(Context context, PropertyServiceAPI serviceAPI);
    }

    /**
     * Inizializza Retrofit in un background thread.
     * Chiama 'getRetrofitInstance2' senza il parametro 'utility'.
     */
    public void initializeRetrofit(RetrofitCallback callback) {
        new Thread(() -> {
            RetrofitClient.RetrofitResult result = RetrofitClient.getRetrofitInstance2(context, userRole);

            activity.runOnUiThread(() -> {
                if(result.isSuccess()) {
                    PropertyServiceAPI serviceAPI = result.serviceAPI;
                    callback.onRetrofitInitialized(context, serviceAPI);
                }
                else {
                    if(barHelper != null) { barHelper.hideProgress(); }
                    dialogManager.showErrorDialog(CLASS_TAG, result.error);
                }
            });
        }).start();
    }
}
