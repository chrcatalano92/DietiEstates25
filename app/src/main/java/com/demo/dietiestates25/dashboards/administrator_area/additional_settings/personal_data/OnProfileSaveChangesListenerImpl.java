package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.personal_data;

import android.net.Uri;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.profile_helper.ProfileViewHelper;

public class OnProfileSaveChangesListenerImpl implements ProfileViewHelper.OnProfileChangesListener {
    private final DialogManager dialogManager;

    public OnProfileSaveChangesListenerImpl(ProfileViewHelper viewHelper, DialogManager dialogManager) {
        viewHelper.setOnSaveChangesListener(this);
        this.dialogManager = dialogManager;
    }

    @Override
    public void onProfileImageChanged(Uri imgUri) {
        dialogManager.showInfoDialog("Immagine di Profilo aggiornata sul server");
    }

    @Override
    public void onSaveChanges(int fieldId, String newTxt) {
        if(fieldId == R.id.editTxt_full_name) {
            dialogManager.showInfoDialog("Aggiorno Fullname con: " + newTxt);
        }
        else {
            dialogManager.showInfoDialog("Aggiorno Username con: " + newTxt);
        }
    }
}
