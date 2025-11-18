package com.demo.dietiestates25.support_classes.dialog_utils;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.InputLayoutHelper;
import com.demo.dietiestates25.support_classes.keyboard_manager.SetupKeyboardEvent;
import com.demo.dietiestates25.support_classes.validation.Validation;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.FormatUtils;
import com.demo.dietiestates25.utils.LogUtils;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.utils.ThemeUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;


/**
 * Classe Manager per ORCHESTRARE la Creazione e la Visualizzazione di vari Dialoghi (Alert, Conferma, Input) in tutta l'applicazione.
 * SRP: La sua Sola Responsabilità è ASTRARRE la complessità della costruzione e della gestione dei dialoghi, fornendo un'API semplice (ad esempio, showSuccessDialog).
 * Diventa autosufficiente per quanto riguarda le sue immagini. Rimuovi una dipendenza da ResourceUtils. Se si inserisce 'getDrawable' stai sporcando DialogManager,
 * Questa classe deve occuparsi di mostrare dialoghi, non di caricare drawable o gestire 'Resources.NotFoundException'. Caricare un'immagine è un compito di basso livello (gestione risorse), mentre gestire un dialogo è un compito di alto livello (UI/Logica). Mischiarli riduce la coesione. Non rispetta DRY se carichi un Drawable dentro DialogManager.
 * Invece, è possibile spostare in DialogManager il metodo 'setImgDialog' che è specifico ("Imposta l'immagine di un dialogo con un fallback specifico"). Questo è un dettaglio implementativo della tua logica di dialogo.
 */
public class DialogManager {
    private static final String CLASS_TAG = "DialogManager";
    private final Context context;
    private final DialogHelper dialogHelper;
    private final InputLayoutHelper layoutHelper;

    private static final int ERROR_IMG = R.drawable.alert_img_dialog;
    private static final int SUCCESS_IMG = R.drawable.success_img_dialog;
    private static final int INFO_IMG = R.drawable.img_info_dialog;
    private static final int INFO_DIALOG_TITLE = R.string.info_dialog_title;
    private static final int ALERT_DIALOG_TITLE = R.string.alert_dialog_title;

    public DialogManager(Context context) {
        this.context = context;
        this.dialogHelper = new DialogHelper(context);
        this.layoutHelper = new InputLayoutHelper(context, this);
    }


    /* INTERFACCE CALLBACK */
    public interface DialogCallback {
        void btnAction();
    }

    public interface OfferDialogListener {
        void onOfferSubmitted(long propertyId, double amount, String notes);
    }

    public interface ConfirmationDialogListener {
        void onConfirm(boolean confirmed);
    }

    public interface LogoutCallback {
        void logout();
    }

    /* METODI PUBBLICI (API del Manager) */

    public void showContactUsDialog() {
        try {
            initializeDialog(R.layout.contact_us_dialog);
            View view = dialogHelper.getRootView();

            TextView msgV = view.findViewById(R.id.dialogMessage);
            msgV.setText(ResourceUtils.getXMLString(context, R.string.contact_us_content_dialog));

            closeDefaultActionBtn(R.id.dialogBtn);
            dialogHelper.show();
        } catch (Exception e) {
            Log.e(CLASS_TAG, "Error showing contactUsDialog: " + e.getMessage());
        }
    }

    public void showConfirmationDialog(int imgResId, Object title, Object message, Class<?> className, DialogCallback callback) {
        try {
            initializeDialog(R.layout.dialog);
            View view = dialogHelper.getRootView();

            ImageView imgV = view.findViewById(R.id.imgDialog);
            setDialogImage(imgV, imgResId);

            TextView titleV = view.findViewById(R.id.dialogTitle);
            instanceofTextViewData(titleV, title);

            TextView msgV = view.findViewById(R.id.dialogMessage);
            instanceofTextViewData(msgV, message);

            if(className != null) {
                dialogHelper.setCustomPositiveBtn(R.id.dialogBtn, dialogView -> {
                    dismissDialog();
                    ActivityUtils.openActivity(context, className);
                });
            }
            else if(callback != null) {
                dialogHelper.setCustomPositiveBtn(R.id.dialogBtn, dialogView -> {
                    dismissDialog();
                    callback.btnAction();
                });
            }
            else {
                closeDefaultActionBtn(R.id.dialogBtn);
            }

            dialogHelper.show();
        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing simple dialog: " + e.getMessage()); }
    }

    /**
     * Metodo helper PRIVATO specifico per i dialoghi.
     * Usa ResourceUtils per il lavoro "pesante", ma gestisce il fallback qui.
     */
    private void setDialogImage(ImageView imgV, int imgId) {
        try {
            // Usa il metodo generico di ResourceUtils
            imgV.setImageDrawable(ResourceUtils.getDrawable(context, imgId));
        }
        catch (Exception e) {
            // Fallback specifico per i dialoghi
            imgV.setImageResource(R.drawable.success_img_dialog);
        }
    }

    public void showSuccessDialog(Object title, Object msg, Class<?> className) {
        if (title != null) {
            showConfirmationDialog(SUCCESS_IMG, title, msg, className, null);
        }
        showConfirmationDialog(SUCCESS_IMG, INFO_DIALOG_TITLE, msg, className, null);
    }

    public void showErrorDialog(String classTag, Object titleError, Object msg) {
        showConfirmationDialog(ERROR_IMG, titleError, msg, null, null);
        String errorMessage = convertMessageObjectToString(msg);
        LogUtils.logError(classTag, errorMessage);
    }

    public void showErrorDialog2(String classTag, Object titleError, Object msg, Class<?> className) {
        showConfirmationDialog(ERROR_IMG, titleError, msg, className, null);
        String errorMessage = convertMessageObjectToString(msg);
        LogUtils.logError(classTag, errorMessage);
    }

    public void showErrorDialog(String classTag, Object msg) { showErrorDialog(classTag, ALERT_DIALOG_TITLE, msg); }

    public void showInfoDialog(Object title, Object msg) { showConfirmationDialog(INFO_IMG, title, msg, null, null); }

    public void showInfoDialog(Object msg) { showConfirmationDialog(INFO_IMG, INFO_DIALOG_TITLE, msg, null, null); }

    public void showInfoDialog(Object title, Object msg, DialogCallback callback) { showConfirmationDialog(INFO_IMG, title, msg, null, callback); }



    public void showQuestionDialog(String questionLabel, ConfirmationDialogListener listener) {
        try {
            initializeDialog(R.layout.confirmation_dialog);

            View view = dialogHelper.getRootView();

            TextView question = view.findViewById(R.id.questionLabel);
            question.setText(questionLabel);

            dialogHelper.setCustomNegativeBtn(R.id.btn_no, dialogView -> handleSetChoice(listener, false));

            dialogHelper.setCustomPositiveBtn(R.id.btn_yes, dialogView -> handleSetChoice(listener, true));

            dialogHelper.show();
        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing question dialog: " + e.getMessage()); }
    }

    public void showDialogClientOffer(String propertyTitle, long propertyId, boolean showNotesField, OfferDialogListener listener) {
        try {
            initializeDialog(R.layout.dialog_offer);

            View view = dialogHelper.getRootView();

            TextView titleV = view.findViewById(R.id.dialogTitle);
//            String title = String.format(utility.getXMLString(R.string.label_property_info) + " %s", propertyTitle);
            titleV.setText(propertyTitle);

            TextInputEditText offerInput = view.findViewById(R.id.offerInput);
            View notesInputLayout = view.findViewById(R.id.notesInputLayout);

            TextInputEditText notesInput = view.findViewById(R.id.notesInput);
            View notesSeparator = view.findViewById(R.id.notesSeparator);

            if (showNotesField) {
                notesInputLayout.setVisibility(View.VISIBLE);
                notesSeparator.setVisibility(View.VISIBLE);
            } else {
                notesInputLayout.setVisibility(View.GONE);
                notesSeparator.setVisibility(View.GONE);
            }

            Validation.generalValidation(offerInput, context);

            layoutHelper.registerInput("offerInpDialog", offerInput);
            Validation.generalValidation(offerInput, context);

            dialogHelper.setCustomPositiveBtn(R.id.btn_new_pwd_send, dialogView -> {
                String offerAmountStr = Objects.requireNonNull(offerInput.getText()).toString();

                if(offerAmountStr.isEmpty()) {

                    offerInput.setError(ResourceUtils.getXMLString(context, R.string.input_offer_error));
                    return;
                }

                double offerAmount = Double.parseDouble(offerAmountStr);
                String notes = showNotesField ? Objects.requireNonNull(notesInput.getText()).toString() :"";


                if (listener != null) {
                    listener.onOfferSubmitted(propertyId, offerAmount, notes);
                }
                dismissDialog();
            });

            closeDefaultActionBtn(R.id.dialogClose);
            dialogHelper.show();
        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing clientOffer dialog: " + e.getMessage()); }
    }

    public void showDialogClientRespondToCounteroffer(double counterofferAgent) {
        try {
            initializeDialog(R.layout.dialog_respond_to_counteroffer);

            View view = dialogHelper.getRootView();

            TextInputEditText counterofferAgentValue = view.findViewById(R.id.counteroffer_agent_value);

            TextInputLayout newOfferLayout = view.findViewById(R.id.new_client_offer_layout);
            TextInputEditText newOfferInput = view.findViewById(R.id.new_client_offer_input);

            TextInputEditText notesInput = view.findViewById(R.id.notes_input);

            counterofferAgentValue.setText(getItalianNumberFormat().format(counterofferAgent));

            layoutHelper.registerInput("clientNotes", notesInput);
            layoutHelper.registerInput("newClientOfferInpDialog", newOfferInput);

            Validation.generalValidation(newOfferInput, context);

            SetupKeyboardEvent keyboardEvent = new SetupKeyboardEvent(context);
            keyboardEvent.closeKeyboardWithActionDone(notesInput);

            MutableLiveData<String> clientOfferResponse = new MutableLiveData<>(null);

            closeDefaultActionBtn(R.id.btn_cancel);

            dialogHelper.addAdditionalBtn(R.id.btn_accept, dialogView -> clientOfferResponse.setValue(ResourceUtils.getXMLString(context, R.string.accepted)));
            dialogHelper.addAdditionalBtn(R.id.btn_reject, dialogView -> clientOfferResponse.setValue(ResourceUtils.getXMLString(context, R.string.rejected)));
            dialogHelper.addAdditionalBtn(R.id.btn_make_new_offer, dialogView -> newOfferLayout.setVisibility(View.VISIBLE));

            dialogHelper.setCustomPositiveBtn(R.id.btn_send_response, dialogView -> sendCounterofferRespond(clientOfferResponse, newOfferLayout));

            dialogHelper.show();
        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing client respond to counteroffer dialog: " + e.getMessage()); }
    }

    public void showDialogCounterofferAgent(TextView txtOfferStatus) {
        try {
            initializeDialog(R.layout.dialog_counteroffer);
            View view = dialogHelper.getRootView();

            TextInputLayout counterofferLayout = view.findViewById(R.id.counterofferInputLayout);
            TextInputEditText counterofferInput = view.findViewById(R.id.counterofferInput);
            TextInputEditText counterofferNotes = view.findViewById(R.id.counterofferNotesInput);
            layoutHelper.registerInput("counterofferAgentInp", counterofferInput);
            layoutHelper.registerInput("counterofferNotesAgent", counterofferNotes);

            Validation.generalValidation(counterofferInput, context);

            SetupKeyboardEvent keyboardEvent = new SetupKeyboardEvent(context);
            keyboardEvent.closeKeyboardWithActionDone(counterofferNotes);

            dialogHelper.setCustomPositiveBtn(R.id.btn_send_counteroffer, dialogView -> {
                String counterofferInp = layoutHelper.getString("counterofferAgentInp");
                String notes = layoutHelper.getString("counterofferNotesAgent");

                if(!counterofferInp.isEmpty()) {
                    double counteroffer = Double.parseDouble(counterofferInp);

                    if(!notes.isEmpty()) {
                        Toast.makeText(context, "Counteroffer: " + FormatUtils.formatPrice(counteroffer) + " - Notes: " + notes, Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    }
                    else {
                        Toast.makeText(context, FormatUtils.formatPrice(counteroffer), Toast.LENGTH_SHORT).show();
                    }
                    dismissDialog();
                }
                else {
                    counterofferLayout.setError(ResourceUtils.getXMLString(context, R.string.counteroffer_is_empty));
                }
            });

            dialogHelper.setCustomNegativeBtn(R.id.dialogClose, dialogView -> {
                txtOfferStatus.setText(" - ");
                txtOfferStatus.setTextColor(ThemeUtils.defaultTxtVColor());
                txtOfferStatus.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                dismissDialog();
            });

            dialogHelper.show();
        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing counteroffer Agent dialog: " + e.getMessage()); }
    }

    // Versione per le Activity e per UserDashboardFragment che mostrano il profilo dell'utente.
    public void showProfileDialog(String name, String email, LogoutCallback callback) {
        try {
            initializeDialog(R.layout.dialog_dashboard_profile);
            View view = dialogHelper.getRootView();

            TextView nameV = view.findViewById(R.id.nameTxtV);
            TextView emailV = view.findViewById(R.id.emailTxtV);

            nameV.setText(name);
            emailV.setText(email);

            dialogHelper.setCustomPositiveBtn(R.id.logoutBtn, dialogView -> {
                dismissDialog();
                if (callback != null) {
                    callback.logout();
                }
            });
            closeDefaultActionBtn(R.id.dialogClose);
            dialogHelper.show();

        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing profile dialog: " + e.getMessage()); }
    }

    public void showDialogPwdRecovery() {
        try {
            initializeDialog(R.layout.dialog_pwd_recovery);
            View view = dialogHelper.getRootView();

            TextInputEditText emailInput = view.findViewById(R.id.dialogInputEmail);
            layoutHelper.registerInput("emailPwdRec", emailInput);
            Validation.emailValidation(emailInput, context);

            dialogHelper.setCustomPositiveBtn(R.id.btn_new_pwd_send, dialogView -> {
                String email = layoutHelper.getStringRequired("emailPwdRec");

                if(email.isEmpty()) {
                    Toast.makeText(context, R.string.input_email_error, Toast.LENGTH_SHORT).show();
                }
                else if(Validation.isNotValidEmail(email)) {
                    Toast.makeText(context, R.string.email_error, Toast.LENGTH_SHORT).show();
                }
                else {
                    dismissDialog();
                    String infoMsg = String.format(ResourceUtils.getXMLString(context, R.string.not_implemented_pwd_recovery), email);
                    showInfoDialog(infoMsg);
                }
            });

            closeDefaultActionBtn(R.id.dialogClose);
            dialogHelper.show();
        } catch (Exception e) { Log.e(CLASS_TAG, "Error showing pwdRecovery dialog: " + e.getMessage()); }
    }


    /* METODI HELPER PRIVATI*/

    private String convertMessageObjectToString(Object msg) {
        if (msg instanceof Integer) {
            // Se è un Integer, è un ID risorsa. Convertilo.
            return ResourceUtils.getXMLString(context, (Integer) msg);
        } else if (msg != null) {
            // Altrimenti, è già una Stringa (o toString() di un altro oggetto).
            return msg.toString();
        }
        return "Messaggio di errore nullo";
    }

    private void instanceofTextViewData(TextView txtV, Object data) {
        if(data instanceof Integer) {
            txtV.setText((Integer) data);
        } else {
            txtV.setText((CharSequence) data);
        }
    }

    private void handleSetChoice(ConfirmationDialogListener listener, boolean confirmed) {
        listener.onConfirm(confirmed);
        dismissDialog();
    }

    private void sendCounterofferRespond(MutableLiveData<String> clientResponse, TextInputLayout newOfferLayout) {
        String response = clientResponse.getValue();
        String notes = layoutHelper.getString("clientNotes");

        if(response != null) {
            if(!notes.isEmpty()) {
                Toast.makeText(context, String.format("Client Response: %s with notes: %s", response, notes), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Client Response: " + response, Toast.LENGTH_SHORT).show();
            }
            dismissDialog();
        }

        if (newOfferLayout.getVisibility() == View.VISIBLE) {
            handleNewOfferResponse(newOfferLayout, notes);
        } else if (response == null) {
            Toast.makeText(context, R.string.make_a_choice_to_continue, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNewOfferResponse(TextInputLayout newOfferLayout, String notes) {
        double newClientOffer = layoutHelper.getDouble("newClientOfferInpDialog");

        if (newClientOffer > 0) {
            if (!notes.isEmpty()) {
                Toast.makeText(context, String.format("New client offer: %s with notes: %s", newClientOffer, notes), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "New Client offer: " + newClientOffer, Toast.LENGTH_SHORT).show();
            }

            dismissDialog();
        } else {
            newOfferLayout.setError(ResourceUtils.getXMLString(context, R.string.customer_offer_empty));
        }
    }

    private void closeDefaultActionBtn(int btnId) {
        dialogHelper.addAdditionalBtn(btnId, dialogView -> dismissDialog());
    }

    private void initializeDialog(int layoutResId) {
        dialogHelper.setLayout(layoutResId);
        dialogHelper.setCancelable(false);
        dialogHelper.setBackgroundTransparent(true);
        dialogHelper.setDialogContainerView(null);
    }

    private void dismissDialog() { dialogHelper.dismiss(); }

    /**
     * Helper statico per replicare Utility.getItalianNumberFormat()
     * (Sarebbe meglio in FormatUtils.java)
     */
    private static NumberFormat getItalianNumberFormat() {
        return NumberFormat.getNumberInstance(Locale.ITALY);
    }
}
