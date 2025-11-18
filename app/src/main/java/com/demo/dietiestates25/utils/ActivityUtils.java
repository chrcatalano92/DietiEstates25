package com.demo.dietiestates25.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Classe Utils per la navigazione e la gestione delle Activity.
 * Contiene solo metodi statici (SRP).
 */
public final class ActivityUtils {

    /**
     * Costruttore privato per impedire l'istanziazione (classe 100% statica).
     */
    private ActivityUtils() {
        throw new UnsupportedOperationException("Questa è una classe utils e non può essere istanziata.");
    }

    /**
     * Ottiene un'istanza di Activity da un Context, se possibile.
     * @param context Il Context di input.
     * @return L'Activity, o null se il Context non è un'Activity.
     * */
    public static Activity getActivityFromContext(Context context) {
        if(context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    /**
     *  Apre una nuova Activity.
     * @param context Il Context di partenza.
     * @param className La classe dell'Activity da aprire.
     *  */
    public static void openActivity(Context context, Class<?> className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }

    /**
     * Apre una nuova Activity e pulisce lo stack di navigazione.
     * Ideale per la navigazione dopo un Login/Logout, per impedire all'utente di tornare alla schermata precedente.
     * @param context Il Context di partenza.
     * @param className La classe dell'Activity da aprire.
     */
    public static void openActivityAndClearStack(Context context, Class<?> className) {
        Intent intent = new Intent(context, className);

        // Queste flag dicono ad Android: "Apri questa nuova activity e cancella tutte quelle che c'erano prima".
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * Apre una nuova Activity passando dati.
     * @param context Il Context di partenza.
     * @param className La classe dell'Activity da aprire.
     * @param dataKey La chiave per i dati.
     * @param data I dati da passare (String o Integer).
     */
    public static void openActivityWithDataIntent(Context context, Class<?> className, String dataKey, Object data) {
        Intent intent = new Intent(context, className);

        if(data instanceof String) {
            intent.putExtra(dataKey, (String) data);
        }
        else if(data instanceof Integer) {
            intent.putExtra(dataKey, (Integer) data);
        }
        else {
            throw new IllegalArgumentException("Data type is not supported: " + data.getClass().getSimpleName());
        }
        context.startActivity(intent);
    }

    /* NOTA BENE: 'openActivityWithBundleDataIntent' è stato rimosso perché dipendeva da un 'IntentHelper' non esistente.
    * Se necessario, può essere aggiunto qui in modo simile.*/
    public static void openActivityWithBundleDataIntent(Context context, Class<?> className, Bundle newData) {
        Intent intent = IntentUtils.createIntentWithBundleData(
                context,
                className,
                newData
        );
        context.startActivity(intent);
    }
}