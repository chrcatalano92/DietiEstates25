package com.demo.dietiestates25.support_classes.profile_helper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.ImgPickerHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.FormatUtils;
import com.demo.dietiestates25.utils.ImageLoaderUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe Helper per gestire la logica di interazione della UI del profilo utente.
 * Centralizza la gestione dei click per modificare i campi e cambiare l'immagine.
 * NOTA: Questo pattern è una violazione di MVVM e SOLID (vedi analisi).
 */
public class ProfileViewHelper implements ImgPickerHelper.ImgPickerListener {
    private OnProfileChangesListener saveChangesListener;
    private final Context context;
    private final DialogManager dialogManager;
    private final ShapeableImageView imgProfile;
    private final TextInputEditText editTxtFullName;
    private final TextInputEditText editTxtUsername;
    private final TextInputEditText editTxtEmail;
    private final TextInputEditText editTxtPwd;
    private final TextInputEditText editTxtCurrentRole;
    private final TextInputEditText editTextLastAccess;
    private boolean isEditing = false;
    private TextInputEditText currentlyEditing = null;

    /**
     * Costruttore aggiornato. Rimuove 'Utility' e accetta 'FragmentActivity' (che è un Context).
     */
    public ProfileViewHelper(View rootView, FragmentActivity activity, DialogManager dialogManager, Class<?> classNameToChangePwd) {
        this.context = activity;
        this.imgProfile = rootView.findViewById(R.id.image_view_profile);
        ImgPickerHelper pickerHelper = new ImgPickerHelper(activity, this);

        FloatingActionButton fabChangeImg = rootView.findViewById(R.id.fab_change_img);
        TextInputLayout fullNameLayout = rootView.findViewById(R.id.full_name_layout);
        TextInputLayout usernameLayout = rootView.findViewById(R.id.username_layout);
        editTxtFullName = rootView.findViewById(R.id.editTxt_full_name);
        editTxtUsername = rootView.findViewById(R.id.editTxt_username);
        editTxtEmail = rootView.findViewById(R.id.editTxt_email);
        editTxtPwd = rootView.findViewById(R.id.editTxt_pwd);
        Button btnChangePwd = rootView.findViewById(R.id.btn_change_pwd);
        editTxtCurrentRole = rootView.findViewById(R.id.editTxt_current_role);
        editTextLastAccess = rootView.findViewById(R.id.editTxt_last_access);

        this.dialogManager = dialogManager;

        setupImgListener(fabChangeImg, pickerHelper);
        setupEditListener(fullNameLayout, editTxtFullName);
        setupEditListener(usernameLayout, editTxtUsername);
        setupPwdListener(btnChangePwd, classNameToChangePwd);
    }

    public void setOnSaveChangesListener(OnProfileChangesListener listener) {
        this.saveChangesListener = listener;
    }

    private void setupImgListener(FloatingActionButton fabChangeImg, ImgPickerHelper pickerHelper) {
        fabChangeImg.setOnClickListener(view -> pickerHelper.openImgPicker());
    }

    @Override
    public void onImgSelected(@NonNull Uri imgUri) {
        ImageLoaderUtils.loadImgFromUri(context, imgUri, imgProfile);

        if(saveChangesListener != null) {
            saveChangesListener.onProfileImageChanged(imgUri);
        }
    }

    @Override
    public void onImgError(@NonNull String errorMsg) {
        dialogManager.showErrorDialog("ProfileViewHelper", errorMsg);
    }

    private void setupEditListener(TextInputLayout inputLayout, TextInputEditText editText) {
        if(inputLayout != null) {
            inputLayout.setEndIconOnClickListener(view -> enableEditing(editText));
        }
        checkConfirmEdit(editText);
    }

    private void setupPwdListener(Button btnChangePwd, Class<?> classNameToChangePwd) {
        btnChangePwd.setOnClickListener(view -> ActivityUtils.openActivity(context, classNameToChangePwd));
    }

    private void checkConfirmEdit(TextInputEditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if(currentlyEditing == editText) {
                confirmEdit();
                return true;
            }
            return false;
        });
    }

    private void enableEditing(TextInputEditText editText) {
        if(isEditing && currentlyEditing != editText) {
            cancelEdit();
        }

        isEditing = true;
        currentlyEditing = editText;
        editText.setEnabled(true);
        editText.requestFocus();
        editText.setSelection(Objects.requireNonNull(editText.getText()).length());
    }

    private void confirmEdit() {
        if(shouldHandleEdit()) {
            if(saveChangesListener != null) {
                saveChangesListener.onSaveChanges(currentlyEditing.getId(), Objects.requireNonNull(currentlyEditing.getText()).toString().trim());
            }
            resetEditState();
        }
    }

    private void cancelEdit() {
        if(shouldHandleEdit()) {
            resetEditState();
        }
    }

    private boolean shouldHandleEdit() { return isEditing && currentlyEditing != null; }

    private void resetEditState() {
        currentlyEditing.setEnabled(false);
        isEditing = false;
        currentlyEditing = null;
    }


    /* METODI SETTER */

    public void setProfileImg(String imgUrl) { ImageLoaderUtils.loadImgFromUrl(imgProfile, imgUrl, R.drawable.AdminCircle); }
    public void setEditTxtFullName(String fullName) { editTxtFullName.setText(fullName); }
    public void setEditTxtUsername(String username) { editTxtUsername.setText(username); }
    public void setEditTxtEmail(String email) { editTxtEmail.setText(email); }
    public void setEditTxtPwd(String pwd) { editTxtPwd.setText(pwd); }
    public void setEditTxtCurrentRole(String role) { editTxtCurrentRole.setText(role); }
    public void setEditTextLastAccess(LocalDateTime lastAccess) { editTextLastAccess.setText(FormatUtils.formatLocalDateTime(lastAccess)); }


    /* INTERFACCIA LISTENER PER NOTIFICARE IL CHIAMANTE */
    public interface OnProfileChangesListener {
        void onProfileImageChanged(Uri newImageUri);
        void onSaveChanges(int fieldId, String newValue);
    }
}
