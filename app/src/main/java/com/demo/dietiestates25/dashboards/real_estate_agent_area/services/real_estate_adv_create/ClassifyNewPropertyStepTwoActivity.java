package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ActivityClassifyNewPropertyStepTwoBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.viewmodels.CreatePropertyViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity per lo STEP 2 della Creazione di un annuncio.
 * SRP: Gestisce la selezione della classificazione (Vendita/Affitto) e la salva nel ViewModel condiviso.
 */
public class ClassifyNewPropertyStepTwoActivity extends AppCompatActivity {

    private ActivityClassifyNewPropertyStepTwoBinding binding;
    private CreatePropertyViewModel viewModel;
    private DialogManager dialogManager;
    private List<CheckBox> checkBoxes; // AGGIUNTO: Per la validazione

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClassifyNewPropertyStepTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CreatePropertyViewModel.class);
        dialogManager = new DialogManager(this);

        setupToolbar();
        setupUI();
        setupListeners();
        populateUIFromViewModel(); // AGGIUNTO: Ripopola lo stato
    }



    /* METODI HELPER void */

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.prop_listing_title);
        }
    }

    private void setupUI() {
        /* Usare 'ResourceUtils' per gestire le stringhe XML. */
        String step2 = ResourceUtils.getXMLString(this, R.string.step) + " 2 " + ResourceUtils.getXMLString(this, R.string.of) + " 4";
        binding.content.steps.step.setText(step2);
        binding.content.steps.stepTitle.setText(R.string.step2_title);

        // Raggruppa le checkbox per la validazione (Clean Code)
        checkBoxes = new ArrayList<>();
        checkBoxes.add(binding.content.saleCheckbox);
        checkBoxes.add(binding.content.rentalCheckbox);
        // Le altre sono disabilitate (clickable=false) quindi non serve aggiungerle
    }

    private void setupListeners() {
        binding.content.backAndNextBtn.backBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        binding.content.backAndNextBtn.nextBtn.setOnClickListener(view -> validateAndProceedToStepThree());
    }

    /**
     * Ripopola la UI se i dati esistono già nel ViewModel.
     * Se l'utente torna indietro dallo Step 3, la sua scelta precedente viene ripristinata.
     */
    private void populateUIFromViewModel() {
        String savedClassify = viewModel.classifyProperty.getValue();
        if (savedClassify != null) {
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.getText().toString().equals(savedClassify)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }
    }

    /**
     * Ssotituisce 'InputManagerStepTwo'. La logica è ora responsabilità dell'Activity.
     * Valida l'input e, se corretto, lo salva nel ViewModel e naviga allo Step 3.
     */
    private void validateAndProceedToStepThree() {
        String selectedType = null;

        /* Itera solo sulle CheckBox attive */
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                if (selectedType != null) {
                    dialogManager.showErrorDialog("Step 2", ResourceUtils.getXMLString(this, R.string.only_one_choice));
                    return;
                }
                /* Legge il testo dalla UI, non hardcoded.*/
                selectedType = checkBox.getText().toString();
            }
        }

        if (selectedType == null) {
            dialogManager.showErrorDialog("Step 2", ResourceUtils.getXMLString(this, R.string.nothing_choice));
            return;
        }

        viewModel.classifyProperty.setValue(selectedType);

        startActivity(new Intent(this, NewPropertyTypeStepThreeActivity.class));
    }
}