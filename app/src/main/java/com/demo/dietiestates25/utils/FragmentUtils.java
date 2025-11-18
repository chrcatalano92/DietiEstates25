package com.demo.dietiestates25.utils;

import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.demo.dietiestates25.R;

/**
 * Classe Utils con ALTA COESIONE per la navigazione e gestione dei Fragment.
 * Contiene solo metodi statici (SRP).
 */
public final class FragmentUtils {

    /**
     * Costruttore privato per impedire l'istanziazione
     */
    private FragmentUtils() {
        throw new UnsupportedOperationException("Questa è una classe utils e non può essere istanziata.");
    }

    /**
     * Sostituisce il Fragment corrente nel container principale,
     * @param fm il FragmentManager.
     * @param f Il nuovo Fragment da mostrare.
     */
    public static void replaceFragment(FragmentManager fm, Fragment f) {
        if(fm == null) {
            throw new IllegalArgumentException("FragmentManager non può essere nullo.");
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.commit();
    }

    public static void imgBtnChangeFragment(ImageButton b, FragmentManager fm, Fragment f) { b.setOnClickListener(view -> replaceFragment(fm, f)); }
}
