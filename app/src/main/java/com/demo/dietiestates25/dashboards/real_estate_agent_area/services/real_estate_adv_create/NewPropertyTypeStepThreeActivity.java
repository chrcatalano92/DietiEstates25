package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityNewPropertyTypeStepThreeBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.viewmodels.CreatePropertyViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* InputLayoutHelper viola due principi importanti SRP e BASSO ACCOPPIAMENTO.
* viola SRP: Il problema di InputLayoutHelper è che fa TROPPE COSE DIVERSE. Non è un Helper, è un Tuttofare. Non deve essere un Tuttofare. Non deve mischiare la gestione delle View con la validazione e la segnalazione degli errori.
* viola BASSO ACCOPPIAMENTO: Questo è il problema PEGGIORE. InputLayoutHelper dipende da DialogManager, DialogManager dipende da InputLayoutHelper. Questo si chiama CIRCULAR DEPENDENCY. È un "anti-pattern" molto grave. Rende le classi impossibili da testare (non puoi testare DialogManager senza creare un InputLayoutHelper, e viceversa) e rende la manutenzione un incubo.
* Per la Logica del CLEAN CODE spostare InputManagerStepThree qui dentro. Le Activity hanno l'obbligo di assumersi la Sola Responsabilità di CONTROLLORI DELLA UI: Gestisce le sue CheckBox, esegue la validazione, usa DialogManager, chiama il CreatePropertyViewModel per salvare i dati.*/

/** CLEAN CODE: Questa Activity ora ha la piena responsabilità (SRP) di
 * gestire la UI dello Step 3, validare i dati e aggiornare il ViewModel,
 * rimuovendo la dipendenza circolare e la violazione SRP
 * delle classi InputManagerStep.
 */
public class NewPropertyTypeStepThreeActivity extends AppCompatActivity {
    private ActivityNewPropertyTypeStepThreeBinding binding;
    private CreatePropertyViewModel viewModel; // AGGIUNTO
    private DialogManager dialogManager; // AGGIUNTO
    private List<CheckBox> checkBoxes; // AGGIUNTO per validazione

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewPropertyTypeStepThreeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inizializza ViewModel e Helper
        viewModel = new ViewModelProvider(this).get(CreatePropertyViewModel.class);
        dialogManager = new DialogManager(this);

        setupToolbar();
        setupUI();
        setupListeners();
        populateUIFromViewModel(); // Ripopola i dati se l'utente torna indietro
    }


    /* METODI HELPER PRIVATI */

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.prop_listing_title);
    }

    private void setupUI() {
        String step3 = ResourceUtils.getXMLString(this, R.string.step) + " 3 " + ResourceUtils.getXMLString(this, R.string.of) + " 4";
        binding.content.steps.step.setText(step3);
        binding.content.steps.stepTitle.setText(R.string.step3_title);

        // Raggruppa le checkbox per la validazione
        checkBoxes = new ArrayList<>();
        checkBoxes.add(binding.content.studioCheckbox);
        checkBoxes.add(binding.content.oneBedroom);
        checkBoxes.add(binding.content.twoBedroom);
        checkBoxes.add(binding.content.apartmentOrFlat);
        checkBoxes.add(binding.content.penthouse);
        checkBoxes.add(binding.content.attic);
        checkBoxes.add(binding.content.townhouseOrTerracedHouse);
        checkBoxes.add(binding.content.villa);
        checkBoxes.add(binding.content.farmhouseCh);
    }

    private void setupListeners() {
        binding.content.backAndNextBtn.backBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed()); // Torna allo Step 2
        binding.content.backAndNextBtn.nextBtn.setOnClickListener(view -> validateAndProceedToStepFour());
    }

    /**
     * Se l'utente torna indietro, la checkbox precedentemente selezionata
     * viene ri-selezionata.
     */
    private void populateUIFromViewModel() {
        String savedType = viewModel.typeProperty.getValue();
        if (savedType != null) {
            for (CheckBox cb : checkBoxes) {
                if (cb.getText().toString().equals(savedType)) {
                    cb.setChecked(true);
                    break;
                }
            }
        }
    }

    /**
     * Logica di validazione e navigazione.
     * Sostituisce completamente InputManagerStepThree.
     */
    private void validateAndProceedToStepFour() {
        String selectedType = null;
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                if (selectedType != null) {
                    // Errore: più di una selezione
                    dialogManager.showErrorDialog("Step 3", ResourceUtils.getXMLString(this, R.string.only_one_choice));
                    return;
                }
                selectedType = cb.getText().toString();
            }
        }

        if (selectedType == null) {
            // Errore: nessuna selezione
            dialogManager.showErrorDialog("Step 3", ResourceUtils.getXMLString(this, R.string.nothing_choice));
            return;
        }

        // 1. Salva i dati nel ViewModel condiviso
        viewModel.typeProperty.setValue(selectedType);

        // 2. Naviga allo Step 4 (senza Bundle)
        startActivity(new Intent(this, DefineExactLocationStepFourActivity.class));
    }
}