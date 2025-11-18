package com.demo.dietiestates25.utils;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.Toast;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.ViewHelpMsg;

/**
 * Classe Utils per fornire feedback rapidi all'utente (esempio, Toast).
 */
public final class FeedbackUtils {

    private FeedbackUtils() { }

    /**
     * Mostra un Toast "Funzionalità non implementata".
     */
    public static void notImplemented(Context context) {
        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mostra un Toast con il 'contentDescription' di un ImageButton al long-press (per messaggi di aiuto).
     */
    public static void showToastHelpMsg(Context context, ImageButton imgBtn) {
        ViewHelpMsg.longPress(imgBtn, description ->
                Toast.makeText(context, description, Toast.LENGTH_SHORT).show()
        );
    }
}
