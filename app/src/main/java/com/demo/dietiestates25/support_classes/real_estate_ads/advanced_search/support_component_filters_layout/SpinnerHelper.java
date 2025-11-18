package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import android.content.Context;

import android.graphics.Typeface;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.MyViewModel;
import com.demo.dietiestates25.utils.ResourceUtils;


/* SpinnerHelper è una classe di utility statica (dovrebbe essere public final class).
La sua Sola Responsabilità (SRP) è applicare la logica di business specifica a uno Spinner usato per la selezione della tipologia di annuncio (Vendita/Affitto).
 - Logica: Ascolta l'evento onItemSelected. Se l'utente seleziona un valore valido (non il placeholder), aggiorna il MyViewModel e cambia il colore del testo.
 - Nome: Il nome "SpinnerHelper" è un po' generico. Un nome più specifico (SRP) potrebbe essere ClassifyPropertySpinnerManager o FilterSpinnerListener, ma "SpinnerHelper" è accettabile se usato solo in questo contesto. */

/**
 * Classe Helper STATICA (final) con la responsabilità (SRP) di
 * configurare il listener di uno Spinner per i filtri di ricerca.
 */
public final class SpinnerHelper {

    /* Costruttore privato */
    private SpinnerHelper(){}

    /**
     * Applica un listener a uno Spinner per aggiornare il MyViewModel e cambiare colore al testo selezionato.
     *
     * @param context Context per accedere alle risorse
     * @param spinner Lo Spinner da configurare
     * @param model Il ViewModel da aggiornare
     */
    public static void spinner(Context context, Spinner spinner, MyViewModel model) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = parent.getItemAtPosition(pos).toString();

                // CORRETTO: Usa ResourceUtils per ottenere la stringa di confronto
                String emptyFilter = ResourceUtils.getXMLString(context, R.string.empty_filter);

                if(!item.equals(emptyFilter) && (pos < parent.getCount() - 2)) {
                    setTxtColor(context, view);
                    model.setClassifyProperty(item);
                }
                else {
                    if(pos >= parent.getCount() - 2) {
                        spinner.setSelection(pos - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not used
            }
        });
    }

    /**
     * Metodo helper privato per impostare lo stile del testo dello Spinner.
     */
    public static void setTxtColor(Context context, View view) {
        // Il 'view' passato da onItemSelected può essere nullo
        if (view instanceof TextView) {
            TextView txtV = (TextView) view;
            txtV.setTextColor(ContextCompat.getColor(context, R.color.Text));
            txtV.setTypeface(txtV.getTypeface(), Typeface.BOLD);
        }
    }
}
