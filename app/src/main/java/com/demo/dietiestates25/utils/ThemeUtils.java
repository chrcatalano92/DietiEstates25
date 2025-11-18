package com.demo.dietiestates25.utils;

import android.graphics.Color;

import com.demo.dietiestates25.support_classes.ThemeHelper;

/**
 * Classe Utils per la gestione di logica legata al Tema dell'app (es. colori dinamici).
 */
public final class ThemeUtils {

    private ThemeUtils() { }

    /**
     * Ritorna il colore di default per il testo (Bianco o Nero) in base al tema (NightMode o LightMode).
     * Metodo STATICO.
     */
    public static int defaultTxtVColor() {
        return ThemeHelper.isNightMode() ? Color.WHITE : Color.BLACK;
    }
}