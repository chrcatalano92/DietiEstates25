package com.demo.dietiestates25.support_classes;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Helper per la gestione di un gruppo di input (campi di un form).
 * È un Anti-Pattern "God Object" travestito da Helper. Non è una classe Utils (perché ha uno stato e non è final static) e non è un Helper pulito.
 * Tenta di fare il lavoro di un ViewModel e di un Validator. La sua responsabilità (presunta) è centralizzare (DRY) la loggica di gestione di un form (registerInput, getStringRequired, validationCheckBoxes).
 * Centralizza la registrazione, il recupero dei valori e la validazione.
 */
public class InputLayoutHelper {
    private static final String CLASS_TAG = "InputLayoutHelper";
    private final Context context;
    private final DialogManager dialogManager;
    private final Map<String, TextView> textViewMap = new HashMap<>();
    private final Map<String, TextInputEditText> txtMap = new HashMap<>();
    private final Map<String, CheckBox> checkBoxMap = new HashMap<>();
    private final Map<String, Spinner> spinnerMap = new HashMap<>();

    /* COSTRUTTORE */
    public InputLayoutHelper(Context context, DialogManager dialogManager) {
        this.context = context;
        this.dialogManager = dialogManager;
    }

    /**
     * Registra un componente di input associandolo a una chiave (stringa).
     */
    public <T> void registerInput(String key, T input) {
        if(key == null || input == null) {
            throw new IllegalArgumentException("Key and input cannot be null");
        }
        if(input instanceof TextInputEditText) {
            txtMap.put(key, (TextInputEditText) input);
        }
        else if(input instanceof CheckBox) {
            checkBoxMap.put(key, (CheckBox) input);
        }
        else if (input instanceof Spinner) {
            spinnerMap.put(key, (Spinner) input);
        }
        else if (input instanceof TextView) {
            textViewMap.put(key, (TextView) input);
        }
        else {
            throw new IllegalArgumentException("Unsupported input type: " + input.getClass());
        }
    }

    public TextView getTextViewId(String key) {
        if(!textViewMap.containsKey(key)) {
            throw new IllegalArgumentException("No TextView registered with key: " + key);
        }
        return textViewMap.get(key);
    }

    public TextInputEditText getTextInputEditTextId(String key) {
        if(!txtMap.containsKey(key)) {
            throw new IllegalArgumentException("No TextInputEditText registered with key: " + key);
        }
        return txtMap.get(key);
    }

    public Spinner getSpinnerId(String key) {
        if(!spinnerMap.containsKey(key)) {
            throw new IllegalArgumentException("No Spinner registered with key: " + key);
        }
        return spinnerMap.get(key);
    }

    public String getString(String key) {
        TextInputEditText editText = getTextInputEditTextId(key);
        return (editText != null && editText.getText() != null) ? editText.getText().toString().trim() : null;
    }

    public String getStringRequired(String key) {
        TextInputEditText editText = getTextInputEditTextId(key);
        if(editText != null && editText.getText() != null) {
            String txt = editText.getText().toString().trim();
            if(txt.isEmpty()) {
                showError(editText, ResourceUtils.getXMLString(context, R.string.field_required));
            }
            return txt;
        }
        return null;
    }

    public String getStringSpinner(String key) {
        Spinner spinner = getSpinnerId(key);
        if(isPlaceHolder(spinner)) {
            String msgErr = ResourceUtils.getXMLString(context, R.string.spinner_required_action) + " " + spinnerMap.get(key);
            dialogManager.showErrorDialog(CLASS_TAG, msgErr);
            return null;
        }
        return spinner.getSelectedItem().toString();
    }

    private boolean isPlaceHolder(Spinner spinner) { return spinner.getSelectedItemPosition() == 0; }

    public Integer getInteger(String key) {
        String string = getString(key);
        return (string == null || string.isEmpty()) ? null : Integer.parseInt(string);
    }

    public int getIntegerRequired(String key) {
        try {
            String input = getStringRequired(key);
            return Integer.parseInt(input);
        }
        catch (NumberFormatException e) {
            showError(txtMap.get(key), ResourceUtils.getXMLString(context, R.string.field_integer_required));
        }
        return 0;
    }

    public double getDouble(String key) {
        try {
            String input = getStringRequired(key);
            if(!input.isEmpty()) {
                String normalized = input.trim().replace(',', '.');
                return Double.parseDouble(normalized);
            }
            else {
                showError(txtMap.get(key), ResourceUtils.getXMLString(context, R.string.field_required));
            }
        } catch (NumberFormatException e) {
            showError(txtMap.get(key), ResourceUtils.getXMLString(context, R.string.field_value_required));
        }
        return 0;
    }

    private boolean allCheckBoxesUnchecked() {
        for(CheckBox checkBox : checkBoxMap.values()) {
            if(checkBox.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private boolean isSingleCheckBoxChecked() {
        int checkedCount = 0;
        for(CheckBox checkBox : checkBoxMap.values()) {
            if(checkBox.isChecked()) {
                checkedCount++;

                if(checkedCount > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getSingleCheckBoxKey() {
        String key = null;
        int checkedCount = 0;

        for(Map.Entry<String, CheckBox> entry : checkBoxMap.entrySet()) {
            if(entry.getValue().isChecked()) {
                checkedCount++;
                key = entry.getKey();

                if(checkedCount > 1) {
                    return null;
                }
            }
        }
        return (checkedCount == 1) ? key : null;
    }

    public String validationCheckBoxes(String classTag) {
        if(allCheckBoxesUnchecked()) {
            dialogManager.showErrorDialog(classTag, ResourceUtils.getXMLString(context, R.string.nothing_choice));
        } else if(isSingleCheckBoxChecked()) {
            return getSingleCheckBoxKey();
        }
        else {
            dialogManager.showErrorDialog(classTag, ResourceUtils.getXMLString(context, R.string.only_one_choice));
        }
        return null;
    }

    public boolean isEmpty(Object data) {
        if(data == null) {
            return true;
        }
        if(data instanceof String) {
            return (((String) data).trim()).isEmpty();
        }
        else if(data instanceof Number) {
            if(data instanceof Double || data instanceof Float) {
                return (((Number) data).doubleValue()) == 0.0;
            }
            return ((Number) data).longValue() == 0;
        }
        else if(data.getClass().isArray()) {
            return Array.getLength(data) == 0;
        }

        return false;
    }

    public void showError(TextInputEditText editText, String msg) {
        if(editText != null) {
            editText.setError(msg);
            editText.setHintTextColor(Color.RED);
            editText.requestFocus();
        }
        else {
            dialogManager.showErrorDialog(CLASS_TAG, msg);
        }
    }
}