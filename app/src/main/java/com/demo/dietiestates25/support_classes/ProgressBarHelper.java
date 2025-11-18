package com.demo.dietiestates25.support_classes;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Classe Helper.
 * ALTA COESIONE (SRP): La sua Sola Responsabilità è gestire la visualizzazione di una ProgressBar bloccando/sbloccando l'interazione dell'utente (FLAG_NOT_TOUCHABLE).
 * BASSO ACCOPPIAMENTO: Dipende solo da Activity e ProgressBar, che gli vengono passati.
 * CLEAN CODE: La logica isShowing previene chiamate duplicate. I metodi blockUserInteraction e allowUserInteraction sono helper privati puliti (DRY).
 */
public class ProgressBarHelper {
    private static final int WINDOW_FLAG_NOT_TOUCHABLE = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    public static final String TAG = "ProgressBarHelper";
    private final Activity activity;
    private final ProgressBar progressBar;
    private boolean isShowing;

    public ProgressBarHelper(@NonNull Activity activity, @NonNull ProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;
        this.isShowing = false;
    }

    public void showProgress() {
        if(isShowing) {
            Log.d(TAG, "Progress already showing");
            return;
        }

        activity.runOnUiThread(() -> {
            if (!isShowing) {
                setVisibilityProgress(View.VISIBLE);
                isShowing = true;
                blockUserInteraction();
            }
        });
    }

    public void hideProgress() {
        if(!isShowing) {
            Log.d(TAG, "Progress already hidden");
            return;
        }

        activity.runOnUiThread(() -> {
            if (isShowing) {
                setVisibilityProgress(View.GONE);
                isShowing = false;
                allowUserInteraction();
            }
        });
    }

    private void setVisibilityProgress(int visibility) { progressBar.setVisibility(visibility); }

    private void blockUserInteraction() {
        try {
            activity.getWindow().setFlags(WINDOW_FLAG_NOT_TOUCHABLE, WINDOW_FLAG_NOT_TOUCHABLE);
        } catch(Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private void allowUserInteraction() {
        try {
            activity.getWindow().clearFlags(WINDOW_FLAG_NOT_TOUCHABLE);
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }
}
