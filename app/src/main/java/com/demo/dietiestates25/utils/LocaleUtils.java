package com.demo.dietiestates25.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.preference.PreferenceManager;

import java.util.Locale;

/**
 * Classe Utils (statica e final) per gestire la localizzazione e le preferenze di lingua dell'applicazione.
 * SRP: Ha la Sola Responsabilità di centralizzare la logica di lettura e scrittura della lingua.
 */
public final class LocaleUtils {

    public static final String LOCALE_KEY = "default";

    private LocaleUtils(){}

    private static void setLocale(Context context, String language) {
        setPersistedData(context, language);

        Configuration conf = getConfiguration(context);

        updateResources(language, conf);
    }

    private static void setPersistedData(Context context, String lang) {
        SharedPreferences pref = getDefSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LOCALE_KEY, lang);
        editor.apply();
    }

    public static void updateResources(String language, Configuration conf) {
        Locale newLocale = Locale.forLanguageTag(language);

        setCurrentLocale(newLocale);

        setCurrentConfiguration(newLocale, conf);
    }

    private static void setCurrentLocale(Locale newLocale) {
        Locale.setDefault(newLocale);
    }

    private static void setCurrentConfiguration(Locale newLocale, Configuration conf) {
        conf.setLocale(newLocale);
        conf.setLayoutDirection(newLocale);
    }

    public static Configuration getConfiguration(Context context) {
        Resources resources = context.getResources();
        return resources.getConfiguration();
    }

    public static String getCurrentLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences pref = getDefSharedPref(context);
        return pref.getString(LOCALE_KEY, defaultLanguage);
    }

    private static SharedPreferences getDefSharedPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void applyLanguage(Activity activity, String language) {
        setLocale(activity, language);

        Intent intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.startActivity(intent);
    }
}
