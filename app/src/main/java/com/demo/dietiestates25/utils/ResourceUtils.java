package com.demo.dietiestates25.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

/**
 * Classe Utils con Alta Coesione per l'accesso alle risorse dell'app (Drawable, String).
 * Contiene solo metodi statici (SRP).
 */
public final class ResourceUtils {

    /**
     * Costruttore privato per impedire l'istanziazione (classe 100% statica).
     */
    private ResourceUtils() { }

    /**
     * Ottiene una stringa dalle risorse XML.
     * @param context Il Context per accedere alle risorse.
     * @param stringId L'ID della stringa (es. R.string.mia_stringa).
     * @return La stringa.
     */
    public static String getXMLString(Context context, int stringId) {
        return context.getString(stringId);
    }

    /**
     * Ottiene un'immagine Drawable dalle risorse (metodo sicuro e non deprecato) Metodo generico e riutilizzabile..
     * @param context Il Context per accedere alle risorse.
     * @param drawableId L'ID del drawable (es. R.drawable.mia_icona).
     * @return Il Drawable.
     */
    public static Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }
}
