package com.demo.dietiestates25.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create.collecting_data_steps.BundleKeys;

import java.util.Objects;

/**
 * Classe Utils (statica e final) per estrarre dati e creare Intent/Bundle.
 * SRP: Centralizza tutte le operazioni relative a Intent e Bundle.
 */
public final class IntentUtils {
    private static final String TAG_IMG_URI = BundleKeys.IMG_URI;

    private IntentUtils() { }


    /* METODI DI LETTURA */

    public static String getStringIntent(Intent intent, String key) {
        if (intent == null || intent.getExtras() == null) return null;
        return intent.getExtras().getString(key);
    }

    public static int getIntIntent(Intent intent, String key) {
        if (intent == null) return 0;
        return intent.getIntExtra(key, 0);
    }

    public static double getDoubleIntent(Intent intent, String key) {
        if (intent != null && intent.getExtras() != null) {
            return intent.getExtras().getDouble(key, 0.0);
        }
        return 0.0;
    }

    public static boolean getBooleanIntent(Intent intent, String key) {
        return Objects.requireNonNull(intent.getExtras()).getBoolean(key);
    }


    // --- METODI DI SCRITTURA (spostati da support_classes) ---

    public static Intent createIntentWithBundleData(Context context, Class<?> nextActivity, Bundle newAccumulator) {
        Intent intent = new Intent(context, nextActivity);
        if(newAccumulator != null && !newAccumulator.isEmpty()) {
            intent.putExtras(newAccumulator);
        }
        return intent;
    }

    public static Bundle updateBundleData(Bundle previousData) {
        Bundle updateData = new Bundle();
        if(previousData != null) {
            updateData.putAll(previousData);
        }
        return updateData;
    }

    public static void addImgToIntent(Intent intent, Uri imgUri) {
        if(intent != null && imgUri != null) {
            intent.putExtra(TAG_IMG_URI, imgUri.toString());
        }
    }

    public static Uri getImgFromIntent(Intent intent) {
        if(intent != null && intent.hasExtra(TAG_IMG_URI)){
            return Uri.parse(intent.getStringExtra(TAG_IMG_URI));
        }
        return null;
    }
}
