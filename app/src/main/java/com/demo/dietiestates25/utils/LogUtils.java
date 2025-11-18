package com.demo.dietiestates25.utils;

import android.util.Log;

/**
 * Classe Utils (WRAPPER) per centralizzare le chiamate al Log di Android.
 * SRP: La sua Sola Responsabilità è LOGGARE.
 * Non sa nulla di Risorse, Context o Tipi di dati, solo Stringhe.
 */
public final class LogUtils {

    private LogUtils() { }

    /**
     * Logga un messaggio di errore.
     * @param classTag Il tag della classe (ad esempio, MyActivity)
     */
    public static void logError(String classTag, String message) {
        if(message == null) {
            message = "Messaggio di log nullo";
        }
        Log.e(classTag, message);
    }

    /**
     * Logga un messaggio di errore con un'eccezione (Throwable).
     * @param classTag Il tag della classe
     * @param message Un messaggio descrittivo
     * @param throwable L'eccezione da loggare (stack trace)
     */
    public static void logError(String classTag, String message, Throwable throwable) {
        Log.e(classTag, message, throwable);
    }

    // Aggiungi altri metodi se necessario (logDebug, logInfo, ecc.)
    public static void logDebug(String classTag, String message) {
        Log.d(classTag, message);
    }
}