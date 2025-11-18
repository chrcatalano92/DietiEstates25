package com.demo.dietiestates25.utils;

import android.content.Context;
import android.os.Build;

import com.demo.dietiestates25.R;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Classe Utils per la formattazione di dati (Date, Prezzi) in Stringhe.
 * Logica di business PURA (o quasi, per il Locale).
 */
public final class FormatUtils {

    private FormatUtils() { }

    /**
     * Formatta un prezzo double in una stringa di valuta (esempio, € 1.200,00).
     * @param price Il prezzo da formattare.
     */
    public static String formatPrice(double price) {
        // NOTA BENE: Questa logica di formattazione del prezzo deve usare Locale.ITALY di default per la valuta Euro.
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.ITALY);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        // Restituisci la stringa formattata
        return nf.format(price);
    }

    /**
     * Formatta un oggetto LocalDateTime in una stringa leggibile (richiede API da 0 a 26+).
     * @param lastUpdated L'oggetto LocalDateTime da formattare.
     */
    public static String formatLocalDateTime(LocalDateTime lastUpdated) {
        String formattedDate = null;

        if (lastUpdated != null && isAtLeastAndroid0()) {
            DateTimeFormatter formatter = getDateTimeFormatter();
            formattedDate = lastUpdated.format(formatter);
        }
        return formattedDate;
    }

    /**
     * Formatta una stringa ISO DateTime (richiede API 26+)
     */
    public static String formatIsoDateTimeString(String lastUpdated) {
        String formattedDate = "";
        if(lastUpdated == null || lastUpdated.isEmpty()) return formattedDate;

        try {
            if (isAtLeastAndroid0()) {
                LocalDateTime dateTime = LocalDateTime.parse(lastUpdated);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                formattedDate = dateTime.format(formatter);
            }
        } catch (Exception e) {
            return lastUpdated; // Ritorna la string aoriginale se il PARSING fallisce.
        }
        return formattedDate;
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        if (!isAtLeastAndroid0()) {
            throw new IllegalStateException("DateTimeFormatter required Android 0 or higher.");
        }
        return DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
    }

    /**
     * Formatta una data e un tempo in una stringa di offerta.
     * @param context Usato per ottenere la stringa di formato.
     */
    public static String formatOfferTimestamp(Context context, String date, String time) {
        String formatString = context.getString(R.string.offer_sent_on);
        return String.format(formatString, date, time);
    }


    /* HELPER METHOD */
    private static boolean isAtLeastAndroid0() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
