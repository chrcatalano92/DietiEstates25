package com.demo.dietiestates25.utils;

import android.content.Context;
import android.widget.Toast;
import com.demo.dietiestates25.R;

/**
 * Classe Utils con Costruttore private, ha solo metodi public static, è stateless (cioè, non ha campi di istanza).
 * SRP: La SOLA RESPONSABILITA' di mostrare messaggi Toast comuni.
 * Questo centralizza la logica dei messaggi e rispetta il principio DRY.
 */
public final class ToastUtils {

    /**
     * Costruttore privato per prevenire l'istanziazione.
     * Questa è una classe di utilità con metodi statici.
     */
    private ToastUtils() { }

    /**
     * Mostra il messaggio standard di successo per il logout.
     */
    public static void showLogoutSuccessToast(Context context) {
        if (context != null) {
            Toast.makeText(context, R.string.logout_success_message, Toast.LENGTH_SHORT).show();
        }
    }
}