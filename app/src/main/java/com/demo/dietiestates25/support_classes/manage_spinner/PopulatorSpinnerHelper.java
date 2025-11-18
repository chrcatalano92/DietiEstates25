package com.demo.dietiestates25.support_classes.manage_spinner;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.utils.ListUtils;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Helper Non Statico (di istanza).
 * ALTA COESIONE (SRP): La sua Sola Responsabilità è gestire il ciclo di vita di un singolo Spinner dinamico. Astrae la logica di:
 *  1. Inizializzare l'adapter (spinnerInit).
 *  2. Aggiornare dinamicamente la lista (updateItemsSpinner) mantenendo il placeholder.
 *  3. Gestire gli errori (handleError).
 *  LEGGE DI DEMETRA VIOLATA. Lq classe crea intenzionalmente dipendenze (new ArrayListUtils(), new DialogManafer(context).
 *  In un'architettura più avanzata (come Hilt/Dagger), queste dipendenze verrebbero iniettate nel costruttore. Ma per questo progetto, è una soluzione pragmatica e accettabile, anche se non pura.
 */
public class PopulatorSpinnerHelper {
    private final Context context;
    private final Spinner spinner;
    private List<String> stringList;
    private ArrayAdapter<String> adapter;
    private final DialogManager dialogManager;

    public PopulatorSpinnerHelper(Context context, Spinner spinner, List<String> stringList) {
        this.context = context;
        this.spinner = spinner;
        this.stringList = stringList;
        this.dialogManager = new DialogManager(context);

        spinnerInit();
        setBackgroundSpinnerPopup();
    }

    private void spinnerInit() {
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setBackgroundSpinnerPopup() {
        if(spinner instanceof androidx.appcompat.widget.AppCompatSpinner) {
            AppCompatSpinner compatSpinner = (AppCompatSpinner) spinner;
            compatSpinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_rectangle));
        }
    }

    public void addNewItemToSpinner(String item) { stringList.add(item); }

    public void updateItemsSpinner(List<String> items) {

        /* Chiama il metodo statico direttamente sulla classe 'ListUtils' invece che su un'istanza. */
        String placeholder = ListUtils.resetItemsList(stringList);

        List<String> newItemsList = new ArrayList<>();
        newItemsList.add(placeholder);
        newItemsList.addAll(items);

        this.stringList = newItemsList;

        adapter.clear();
        adapter.addAll(newItemsList);
        adapter.notifyDataSetChanged();
    }

    public String getSelectedItemSpinner() { return (spinner != null) ? spinner.getSelectedItem().toString() : ""; }

    public void handleError(Exception e) { dialogManager.showErrorDialog("PopulatorSpinnerHelper", e.getMessage()); }
}
